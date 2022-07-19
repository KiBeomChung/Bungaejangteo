package com.example.demo.src.favorite;

import com.example.demo.src.favorite.model.PostFavoriteStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PostFavoriteStoreRes addFavorite(int followingId, int followerId){

        String lastInsertIdStr = favoriteDao.addFollowing(followingId);
        System.out.println("lastInsertIdStr : " + lastInsertIdStr);
        int lastInsertId = Integer.parseInt(lastInsertIdStr);
        System.out.println("lastInsertId : " + lastInsertId);
        String result = favoriteDao.receiveFollowing(followerId, lastInsertId);
        System.out.println("Service Result : " + result);

        return new PostFavoriteStoreRes(result);
    }
}
