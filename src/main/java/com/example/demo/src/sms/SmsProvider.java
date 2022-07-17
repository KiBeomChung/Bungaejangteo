package com.example.demo.src.sms;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class SmsProvider {

    private final SmsDao smsDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SmsProvider(SmsDao smsDao, JwtService jwtService) {
        this.smsDao = smsDao;
        this.jwtService = jwtService;
    }

    public  int checkExistingUser(String phoneNum) throws BaseException {
        try {
            int result = smsDao.checkExistingUser(phoneNum);
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkAuth(String phoneNum,String code) throws BaseException {
        try {
            return(smsDao.checkAuth(phoneNum));

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
