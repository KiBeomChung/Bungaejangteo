package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LikeProvider {
    private final LikeDao likeDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LikeProvider(LikeDao likeDao, JwtService jwtService) {
        this.likeDao = likeDao;
        this.jwtService = jwtService;
    }

    public List<GetCollectionProductsRes> getCollectionProducts(int userIdx, int collectionIdx, String status) throws BaseException {
        try {
            List<GetCollectionProductsRes> getProductRes = likeDao.getCollectionProducts(userIdx,collectionIdx,status);
            return getProductRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
