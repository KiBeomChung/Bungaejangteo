package com.example.demo.src.favorite;

import com.example.demo.src.favorite.model.GetFavoriteUserDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserProductsDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<GetFavoriteUserRes> getFavoriteUserResList(int userId) {

        List<GetFavoriteUserDetailRes> getFavoriteUserDetailResList = favoriteDao.getFavoriteUserDetailList(userId);
//        List<GetFavoriteUserProductsDetailRes> getFavoriteUserProductsDetailResList =
//                favoriteDao.getFavoriteUserProductsDetailList(userId);

        List<GetFavoriteUserRes> getFavoriteUserRes = new ArrayList<>();

        GetFavoriteUserRes getFavoriteUserRes1 = new GetFavoriteUserRes(getFavoriteUserDetailResList);
        getFavoriteUserRes.add(getFavoriteUserRes1);

        return getFavoriteUserRes;
    }
}
