package com.example.demo.src.sms;

import com.example.demo.src.sms.SmsProvider;
import com.example.demo.src.sms.SmsService;
import com.example.demo.src.user.model.GetUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.sms.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Random;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPhonNum;

@RestController
@RequestMapping("/app/sms")
public class SmsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SmsProvider smsProvider;
    @Autowired
    private final SmsService smsService;
    @Autowired
    private final JwtService jwtService;

    public SmsController(SmsProvider smsProvider, SmsService smsService, JwtService jwtService){
        this.smsProvider = smsProvider;
        this.smsService = smsService;
        this.jwtService = jwtService;
    }

    /**
     * 본인인증 문자 발송 API
     * [POST] /sms
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createAuth(@RequestBody PostSMSReq postSMSReq){
        try {
            //휴대폰번호 비었는지 확인
            {
                if (postSMSReq.getPhoneNum()==null) {
                    return new BaseResponse<>(EMPTY_PHONENUM);
                }

                //휴대폰번호 숫자로만 구성되었는지 확인
                boolean isNumeric = postSMSReq.getPhoneNum().matches("[+-]?\\d*(\\.\\d+)?");
                if (!isNumeric) {
                    return new BaseResponse<>(INCORRECT_TYPEOF_PHONENUM);
                }

                //휴대폰번호 형태 확인
                if (!isRegexPhonNum(postSMSReq.getPhoneNum())) {
                    return new BaseResponse<>(INCORRECT_SHAPEOF_PHONENUM);
                }
            }


            System.out.println(postSMSReq.getPhoneNum());
            smsService.createAuth(postSMSReq.getPhoneNum());
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getMessage()));
        } catch (CoolsmsException exception){
            System.out.println("sms에러");
            return new BaseResponse<>(FAILED_TO_COOLSMS);
        }
    }

    /**
     * 본인 인증 API
     * [GET] sms/:phone?code=
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/{phoneNum}")
    public BaseResponse<String> checkAuth(@PathVariable("phoneNum") String phoneNum,@RequestParam(value="code")  String code) {
        try{

            String realAuth = smsProvider.checkAuth(phoneNum,code);
            if (!realAuth.equals(code)){
                throw new BaseException(FAILED_TO_CHECK_AUTH);
            }
            return new BaseResponse<>(SUCCESS);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}