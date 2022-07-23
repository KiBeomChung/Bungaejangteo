package com.example.demo.src.favorite;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.GetFavoriteUserDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserProductsDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserRes;
import com.example.demo.src.favorite.model.GetFollowingUserRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.NOT_AVALIABLE_USER_STORE;

@Service
public class FavoriteProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FavoriteDao favoriteDao;
    private final JwtService jwtService;
    private final UserDao userDao;

    @Autowired
    public FavoriteProvider(FavoriteDao favoirteDao, JwtService jwtService, UserDao userDao) {
        this.favoriteDao = favoirteDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    public List<GetFavoriteUserRes> getFavoriteUserResList(int userId) throws BaseException {

        try {
            List<GetFavoriteUserRes> getFavoriteUserDetailResList = favoriteDao.getFavoriteUserDetailList(userId);

            for (GetFavoriteUserRes following : getFavoriteUserDetailResList) {
                following.setGetFavoriteUserProductsDetailResList(favoriteDao.getFollowStoreImage(following.getId(), userId));
            }
            return getFavoriteUserDetailResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowingUserRes> getFollowingUserResList(int userId) throws BaseException {

        //내가 이용가능한 상태가 아닌 경우
        if(userDao.checkUserState(userId) == 0) {
            throw new BaseException(NOT_AVALIABLE_USER_STORE);
        }

        try {
            List<GetFollowingUserRes> getFollowingUser = favoriteDao.getFollowingUserList(userId);
            return getFollowingUser;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
