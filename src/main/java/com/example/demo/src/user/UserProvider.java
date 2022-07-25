package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.product.model.GetProductRes;
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
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int checkExisttUser(String phoneNum) throws BaseException {
        try {
            return userDao.checkExisttUser(phoneNum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserRes logIn(PostUserReq postUserReq) throws BaseException {
        try {

            int userIdx = userDao.getUserIdx(postUserReq);
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx, jwt, true);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int checkExistStoreName(String storeName) throws BaseException {
        try {
            return userDao.checkExistStoreName(storeName);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserStoreInfoRes getUserStoreInfo(int id) throws BaseException {

        if (userDao.checkId(id) == 0) { //db에 입력받은 id가 존재하는지 확인
            throw new BaseException(NOT_AVALIABLE_USER_STORE);
        } else {
            GetUserStoreInfoRes getUserStoreInfo = userDao.getUserStoreInfo(id);
            return getUserStoreInfo;
        }
    }

    public GetUserInfoRes getUserInfoRes(int userId) throws BaseException {

        if(userDao.checkUserState(userId) == 1) {
            GetUserInfoRes getUserInfoRes = userDao.getUserInfo(userId);
            return getUserInfoRes;
        } else {
            throw new BaseException(NOT_AVALIABLE_GET_USER_STATE);
        }
    }

    public List<GetUserInquiringRes> getUserInquiringList(int userId) throws BaseException {

        if(userDao.checkUserState(userId) == 0) {
            throw new BaseException(FAILED_TO_LOAD_INQUIRY);
        }
        // 상점 신고 받았는지 or 삭제되지는 않았는지

        List<GetUserInquiringRes> getUserInquiringResList = userDao.getUserInquiring(userId);

        return getUserInquiringResList;

    }

    public GetAnotherUserStoreInfoRes getAnotherUserStoreInfo(int userId, int targetId) {

        // visitNum 먼저 +1
        int stepOne = userDao.addVisitNum(targetId);

        // 그 후 상점 조회
        GetAnotherUserStoreInfoRes getAnotherUserStoreInfoRes = userDao.getAnotherUserStoreInfo(userId, targetId);

        return getAnotherUserStoreInfoRes;
    }

    public List<GetProductRes> getMyPageProducts(int userIdx,String status) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<GetProductRes> getProductRes = userDao.getMyPageProducts(userIdx,status);
            return getProductRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isRemovableUser(int userIdx) throws BaseException {
        try {
            int result = userDao.isRemovableUser(userIdx);
            System.out.println(result);
            return userDao.isRemovableUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isDeletedUser(int userIdx) throws BaseException {
        try {
            int result = userDao.isDeletedUser(userIdx);
            System.out.println(result);
            return userDao.isDeletedUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
