package com.example.demo.src.favorite;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.PostFavoriteStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static sun.misc.Version.println;

@Service
public class FavoriteService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FavoriteDao favoriteDao;
    private final FavoriteProvider favoriteProvider;
    private final JwtService jwtService;

    @Autowired
    public FavoriteService(FavoriteDao favoriteDao, FavoriteProvider favoriteProvider, JwtService jwtService) {
        this.favoriteDao = favoriteDao;
        this.favoriteProvider = favoriteProvider;
        this.jwtService = jwtService;
    }

    public PostFavoriteStoreRes addFavorite(int followingId, int followerId) throws BaseException {

        if (favoriteDao.checkUserStatus(followingId) == 0) { // 해당 상점이 신고 받거나 탈퇴한 상점이 아닌지 확인
            throw new BaseException(NOT_AVALIABLE_ADD_FOLLOW);
        }
        if (favoriteDao.isAlreadyFollow(followingId, followerId) == 1) {
            throw new BaseException(ALREADY_EXIST_FOLLOW);
        }

        try {
            String lastInsertIdStr = favoriteDao.addFollowing(followingId);
            //     System.out.println("lastInsertIdStr : " + lastInsertIdStr);
            int lastInsertId = Integer.parseInt(lastInsertIdStr);
            //     System.out.println("lastInsertId : " + lastInsertId);
            String result = favoriteDao.receiveFollowing(followerId, lastInsertId);
            //     System.out.println("Service Result : " + result);
            return new PostFavoriteStoreRes(result);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }

    public String deleteFavorite(int followingId, int followerId) throws BaseException {

        try {
            int deleteSuccess = favoriteDao.deleteFavorite(followingId, followerId);
            String result = "";

            if (deleteSuccess == 2) {
                result = "팔로우 취소 하였습니다.";
            } else {
                throw new BaseException(FOLLOW_CANCEL_FAIL);
            }
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String addFollowBrand(int userId, int brandId) throws BaseException {

        // 해당 브랜드가 존재하지 않는 경우
        if (favoriteDao.isExistBrand(brandId) == 0) {
            throw new BaseException(NOT_EXISTS_BRAND);
        }
        if (favoriteDao.isDeletedBrand(brandId) == 1) {
            throw new BaseException(NOT_AVALIABLE_BRAND_STATUS);
        }
        // 이미 팔로우를 진행한 경우
        if (favoriteDao.duplicatedFollow(userId, brandId) == 1) {
            throw new BaseException(DUPLICATED_FOLLOW_BRAND);
        }

        try {
            int addFollowBrand = favoriteDao.addFollowBrand(userId, brandId);
            System.out.println(addFollowBrand);
            String result = "";

            if (addFollowBrand == 1) {
                result = "해당 브랜드를 팔로우 하였습니다.";
            } else {
                throw new BaseException(FOLLOW_BRAND_FAIL);
            }
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String deleteFollowBrand(int userId, int brandId) throws BaseException {

        try {
            int deleteFollowBrand = favoriteDao.deleteFollowBrand(userId, brandId);
            String result = "";

            if (deleteFollowBrand == 1) {
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