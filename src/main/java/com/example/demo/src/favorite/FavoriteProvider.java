package com.example.demo.src.favorite;

import com.example.demo.config.BaseException;
import com.example.demo.src.favorite.model.GetFavoriteUserDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserProductsDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserRes;
import com.example.demo.src.favorite.model.GetFollowingUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FavoriteProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FavoriteDao favoriteDao;
    private final JwtService jwtService;

    @Autowired
    public FavoriteProvider(FavoriteDao favoirteDao, JwtService jwtService) {
        this.favoriteDao = favoirteDao;
        this.jwtService = jwtService;
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

    public List<GetFollowingUserRes> getFollowingUserResList(int userId) {

        List<GetFollowingUserRes> getFollowingUser = favoriteDao.getFollowingUserList(userId);
        return getFollowingUser;
    }

}
