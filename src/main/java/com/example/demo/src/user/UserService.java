package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        try {
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx, jwt, false);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createStoreName(int userIdx, String storeName) throws BaseException {
        if (userProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            int result = userDao.createStoreName(userIdx, storeName);

            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_STORENAME);

            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyStoreInfo(PatchUserStoreInfoReq patchUserStoreInfoReq, int id) {
        userDao.modifyStoreInfo(patchUserStoreInfoReq, id);
    }

    public void modifyProductState(PatchProductStateReq patchProductStateReq, int userId, int productsId) throws BaseException {
        try {
            if (userDao.checkProductStateReport(productsId) == 1) {
                throw new BaseException(PATCH_FAIL_REPORT_USER_PRODUCT);
            }
            if (userDao.checkProductStateDelete(productsId) == 1) {
                throw new BaseException(PATCH_FAIL_DELETE_USER_PRODUCT);
            }
            userDao.modifyProductState(patchProductStateReq, userId, productsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserInfo(PatchUserInfoReq patchUserInfoReq, int userId) {
        userDao.modifyUserInfo(patchUserInfoReq, userId);
    }

    public void addInquiring(int inquiringId, int inquiredId, PostUserInquiryReq postUserInquiryReq) throws BaseException {

        if (userDao.checkUserState(inquiringId) == 0) {
            throw new BaseException(FAILED_TO_INQUIRING);
        }
        if (userDao.checkUserState(inquiredId) == 0) {
            throw new BaseException(FAILED_TO_INQUIRED);
        }

        try {
            String lastInsertIdStr = userDao.addInquiring(inquiringId, postUserInquiryReq);
            int connectId = Integer.parseInt(lastInsertIdStr);
            int result = userDao.addInquired(inquiredId, connectId);

            if (result != 1) {
                throw new BaseException(FAILED_TO_WRITE_INQUIRY);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String deleteInquiring(int inquiredId, int inquiringId, PatchUserDeleteInqReq patchUserDeleteInqReq) throws BaseException {

        //문의를 남기지 않았을 경우

        if(userDao.checkInquiringStatus(inquiredId, inquiringId) == 1) {
            throw new BaseException(INQUIRING_ALREADY_DELETED);
        }
        int deleteInquiring = userDao.deleteInquiring(inquiredId, inquiringId, patchUserDeleteInqReq);
        System.out.println("del" + deleteInquiring);
        String result = "";

        try {
            if (deleteInquiring == 2) {
                result = "해당 문의를 삭제하였습니다.";
            } else if (deleteInquiring == 0){
                throw new BaseException(INQUIRING_DOESNT_EXISTS);
            }
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void deleteUser(int userIdx,DeleteUserReq deleteUserReq) throws BaseException {
        if (userProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETE_USER_ALREADY_DELETED_USER);
        }
        if (userProvider.isRemovableUser(userIdx) == 1){
            throw new BaseException(DELETE_USER_NOT_REMOVABLE_USER);
        }

        try {
            userDao.deleteUser(userIdx,deleteUserReq);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserRes createKakaoUser(String name, String phoneNum) throws BaseException {
        try {
            int userIdx = userDao.createKakaoUser(name, phoneNum);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx, jwt, false);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getKakaoUserInfo(String token) throws IOException,BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();


            System.out.println("nickname : " + nickname);


            br.close();
            return nickname;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void logoutUser(int userIdx) throws BaseException {
        if (userProvider.isLogOutUser(userIdx) == 1){
            throw new BaseException(PATCH_USER_ALREADY_LOGOUT_USER);
        }
        try {
            userDao.logoutUser(userIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
