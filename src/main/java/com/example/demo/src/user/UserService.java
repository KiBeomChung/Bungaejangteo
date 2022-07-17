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
        //중복
        if (userProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try {
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try {
            int result = userDao.modifyUserName(patchUserReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createAuth(String phoneNum) throws BaseException, CoolsmsException{

            String api_key = "NCSDCUZHZXY4LW73";
            String api_secret = "TCLU8WCBHQFSWWI5LADNVCJB6VW8WHCJ";
            Message coolsms = new Message(api_key, api_secret);

            Random rand = new Random();
            String numStr = "";
            for (int i = 0; i < 6; i++) {
                String ran = Integer.toString(rand.nextInt(10));
                numStr += ran;
            }


            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to", phoneNum);    // 수신전화번호 (ajax로 view 화면에서 받아온 값으로 넘김)
            params.put("from", "01030467282");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
            params.put("type", "sms");
            params.put("text", "[번개장터] 인증번호 [" + numStr + "]  입력시 정상처리 됩니다.");

            try {
                JSONObject obj = (JSONObject) coolsms.send(params);
                System.out.println(obj.toString());
                if ((obj.get("error_count").toString()).equals("1")) {
                    throw new CoolsmsException("", 1);
                }

            } catch (CoolsmsException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCode());
                throw new CoolsmsException(e.getMessage(),e.getCode());
            }


            try {
                if (userProvider.checkExistingUser(phoneNum) <= 0) {
                    userDao.createAuth(phoneNum, numStr);
                } else {
                    userDao.updateAuth(phoneNum, numStr);
                }
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        }




}
