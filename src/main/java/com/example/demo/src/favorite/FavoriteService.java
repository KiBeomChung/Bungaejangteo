package com.example.demo.src.favorite;

import com.example.demo.config.BaseException;
import com.example.demo.src.favorite.model.PostFavoriteStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.NOT_AVALIABLE_ADD_FOLLOW;

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

        if(favoriteDao.checkUserStatus(followingId) == 0){ // 해당 상점이 신고 받거나 탈퇴한 상점이 아닌지 확인
            throw new BaseException(NOT_AVALIABLE_ADD_FOLLOW);
        }

        String lastInsertIdStr = favoriteDao.addFollowing(followingId);
        //     System.out.println("lastInsertIdStr : " + lastInsertIdStr);
        int lastInsertId = Integer.parseInt(lastInsertIdStr);
        //     System.out.println("lastInsertId : " + lastInsertId);
        String result = favoriteDao.receiveFollowing(followerId, lastInsertId);
        //     System.out.println("Service Result : " + result);

        return new PostFavoriteStoreRes(result);
    }
}
