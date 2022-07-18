package com.example.demo.src.user;

import com.example.demo.src.sms.model.PostSMSReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try{
            if(Email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 본인인증 로그인/회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        if(postUserReq.getPhoneNum() == null){
            return new BaseResponse<>(EMPTY_PHONENUM);
        }

        if(postUserReq.getName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        //휴대폰 정규표현
        if(!isRegexPhonNum(postUserReq.getPhoneNum())){
            return new BaseResponse<>(INCORRECT_SHAPEOF_PHONENUM);
        }
        try{
            if (userProvider.checkExisttUser(postUserReq.getPhoneNum()) == 1) {
                PostUserRes postUserRes = userProvider.logIn(postUserReq);
                return new BaseResponse<>(postUserRes);
            }else{
                PostUserRes postUserRes = userService.createUser(postUserReq);
                return new BaseResponse<>(postUserRes);
            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입-상점이름 설정 API
     * [PATCH] /users/storename
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/storename")
    public BaseResponse<String> createStoreName(@RequestParam(value="storeName")  String storeName){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            System.out.println(storeName);
            if(storeName.equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_STORENAME);
            }

            if(!isRegexLangType(storeName)){
                return new BaseResponse<>(INCORRECT_TYPEOF_STORENAME);
            }

            if (storeName.length()>10){
                return new BaseResponse<>(POST_USERS_LONG_STORENAME);
            }

            if (userProvider.checkExistStoreName(storeName) == 1){
                return new BaseResponse<>(POST_USERS_EXISTS_STORENAME);
            }

            userService.createStoreName(userIdxByJwt,storeName);

            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
