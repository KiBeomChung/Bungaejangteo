package com.example.demo.src.sms;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.sms.SmsDao;
import com.example.demo.src.sms.SmsProvider;
import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.sms.model.*;
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

@Service
public class SmsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SmsDao smsDao;
    private final SmsProvider smsProvider;
    private final JwtService jwtService;


    @Autowired
    public SmsService(SmsDao smsDao, SmsProvider smsProvider, JwtService jwtService) {
        this.smsDao = smsDao;
        this.smsProvider = smsProvider;
        this.jwtService = jwtService;

    }

    public String createAuth(String phoneNum) throws BaseException, CoolsmsException{

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
            if (smsProvider.checkExistingUser(phoneNum) <= 0) {
                smsDao.createAuth(phoneNum, numStr);
            } else {
                smsDao.updateAuth(phoneNum, numStr);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        return numStr;
    }
}
