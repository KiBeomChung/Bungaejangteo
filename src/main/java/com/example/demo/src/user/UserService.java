package com.example.demo.src.user;


import com.example.demo.config.BaseResponse;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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

        //이미 삭제 했을경우
        //문의를 남기지 않았을 경우
        //status 값이 null 인 경우
        //status 값이 inactive가 아닌경우

        try {
            int deleteInquiring = userDao.deleteInquiring(inquiredId, inquiringId, patchUserDeleteInqReq);
            System.out.println("del" + deleteInquiring);
            String result = "";

            if (deleteInquiring == 2) {
                result = "해당 브랜드 팔로우를 취소하였습니다.";
            } else {
                throw new BaseException(FOLLOW_DOESNT_EXISTS);
            }
            return result;
        } catch (Exception exception) {
            throw new BaseException(FOLLOW_DOESNT_EXISTS);
        }
    }
}
