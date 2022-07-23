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


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     *
     * @return BaseResponse<List < GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try {
            if (Email == null) {
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     *
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try {
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 본인인증 로그인/회원가입 API
     * [POST] /users
     *
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        if (postUserReq.getPhoneNum() == null) {
            return new BaseResponse<>(EMPTY_PHONENUM);
        }

        if (postUserReq.getName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        //휴대폰 정규표현
        if (!isRegexPhonNum(postUserReq.getPhoneNum())) {
            return new BaseResponse<>(INCORRECT_SHAPEOF_PHONENUM);
        }
        try {
            if (userProvider.checkExisttUser(postUserReq.getPhoneNum()) == 1) {
                System.out.println(22222);
                PostUserRes postUserRes = userProvider.logIn(postUserReq);
                return new BaseResponse<>(postUserRes);
            } else {
                PostUserRes postUserRes = userService.createUser(postUserReq);
                return new BaseResponse<>(postUserRes);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 회원가입-상점이름 설정 API
     * [PATCH] /users/storename
     *
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PostMapping("/store")
    public BaseResponse<String> createStoreName(@RequestParam(value = "storename") String storeName) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            System.out.println(storeName);
            if (storeName.equals("")) {
                return new BaseResponse<>(POST_USERS_EMPTY_STORENAME);
            }

            if (!isRegexLangType(storeName)) {
                return new BaseResponse<>(INCORRECT_TYPEOF_STORENAME);
            }

            if (storeName.length() > 10) {
                return new BaseResponse<>(POST_USERS_LONG_STORENAME);
            }

            if (userProvider.checkExistStoreName(storeName) == 1) {
                return new BaseResponse<>(POST_USERS_EXISTS_STORENAME);
            }

            userService.createStoreName(userIdxByJwt, storeName);

            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상점 정보 수정 전 정보 가져오는 API
     *
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("modify/stores/{id}")
    public BaseResponse<GetUserStoreInfoRes> getUserStoreInfoRes(@PathVariable("id") int id) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (id != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserStoreInfoRes getUserStoreInfoRes = userProvider.getUserStoreInfo(id);
            return new BaseResponse<>(getUserStoreInfoRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 상점 정보 수정 API
     *
     * @param patchUserStoreInfoReq
     * @param id
     * @return
     * @throws BaseException
     */
    @ResponseBody
    @PatchMapping("modify/stores/{id}")
    public BaseResponse<String> modifyStoreInfo(@RequestBody PatchUserStoreInfoReq patchUserStoreInfoReq,
                                                @PathVariable("id") int id) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (id != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (patchUserStoreInfoReq.getStoreName() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_STORENAME);
            }
            if (patchUserStoreInfoReq.getStoreName().length() > 10) {
                return new BaseResponse<>(PATCH_USERS_LONG_STORENAME);
            }
            if (patchUserStoreInfoReq.getShopUrl() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_SHOPURL);
            }
            if (patchUserStoreInfoReq.getShopUrl().length() > 50) {
                return new BaseResponse<>(PATCH_USERS_LONG_SHOPURL);
            }
            if (!isRegexLangType(patchUserStoreInfoReq.getStoreName())) {
                return new BaseResponse<>(INCORRECT_TYPEOF_STORENAME);
            }
            if (patchUserStoreInfoReq.getContactTime() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_CONTACTTIME);
            }
            if (patchUserStoreInfoReq.getDescription() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_DESCRIPTION);
            }
            if (patchUserStoreInfoReq.getDescription().length() > 1000) {
                return new BaseResponse<>(PATCH_USERS_LONG_DESCRIPTION);
            }
            if (patchUserStoreInfoReq.getPolicy() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_POLICY);
            }
            if (patchUserStoreInfoReq.getPolicy().length() > 1000) {
                return new BaseResponse<>(PATCH_USERS_LONG_POLICY);
            }
            if (patchUserStoreInfoReq.getPrecautions() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_PRECAUTIONS);
            }
            if (patchUserStoreInfoReq.getPrecautions().length() > 1000) {
                return new BaseResponse<>(PATCH_USERS_LONG_PRECAUTIONS);
            }

            userService.modifyStoreInfo(patchUserStoreInfoReq, id);
            String result = "수정 완료하였습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 상점 상품 상태 변경 API
     *
     * @param patchProductStateReq
     * @param userId
     * @param productsId
     * @return
     */
    @ResponseBody
    @PatchMapping("/{userId}/{productsId}/state")
    public BaseResponse<String> modifyProductState(@RequestBody PatchProductStateReq patchProductStateReq,
                                                   @PathVariable("userId") int userId,
                                                   @PathVariable("productsId") int productsId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchProductStateReq patchProductStateReq1 = new PatchProductStateReq(patchProductStateReq.getStatus());
            userService.modifyProductState(patchProductStateReq1, userId, productsId);
            String result = "상태 변경되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 계정 정보 수정 API
     * @param patchUserInfoReq
     * @param userId
     * @return
     */
    @ResponseBody
    @PatchMapping("/{userId}/settings")
    public BaseResponse<String> modifyUserInfo(@RequestBody PatchUserInfoReq patchUserInfoReq,
                                                @PathVariable("userId") int userId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //성별 입력 x
            if(patchUserInfoReq.getGender() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_GENDER);
            }
            if(!(patchUserInfoReq.getGender().equals("남") || patchUserInfoReq.getGender().equals("여"))) {
                return new BaseResponse<>(PATCH_USERS_CORRECT_GENDER);
            }
            if(patchUserInfoReq.getPhoneNum() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_PHONENUM);
            }
            if (!isRegexPhonNum(patchUserInfoReq.getPhoneNum())) {
                return new BaseResponse<>(INCORRECT_SHAPEOF_PHONENUM);
            }
            if(patchUserInfoReq.getBirth() == null) {
                return new BaseResponse<>(PATCH_USERS_EMPTY_BIRTH);
            }
            if(!isRegexBirth(patchUserInfoReq.getBirth())) {
                return new BaseResponse<>(INCORRECT_SHAPEOF_BIRTH);
            }
            userService.modifyUserInfo(patchUserInfoReq, userId);
            String result = "정보 변경하였습니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 계정 설정 전 기존 정보를 가져오는 API
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/{userId}/settings")
    public BaseResponse<GetUserInfoRes> getUserInfoRes(@PathVariable("userId") int userId) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserInfoRes getUserInfoRes = userProvider.getUserInfoRes(userId);
            return new BaseResponse<>(getUserInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/{inquiringId}/inquiring/{inquiredId}")
    public BaseResponse<String> addInquiring(@PathVariable("inquiringId") int inquiringId,
                                             @PathVariable("inquiredId") int inquiredId,
                                             @RequestBody PostUserInquiryReq postUserInquiryReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (inquiringId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postUserInquiryReq.getText() == null ) {
                return new BaseResponse<>(POST_USERS_EMPTY_INQUIRING);
            }
            if(postUserInquiryReq.getText().length() > 100) {
                return new BaseResponse<>(POST_USERS_LONG_INQUIRING);
            }

            userService.addInquiring(inquiringId, inquiredId, postUserInquiryReq);
            String result = postUserInquiryReq.getText();
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}