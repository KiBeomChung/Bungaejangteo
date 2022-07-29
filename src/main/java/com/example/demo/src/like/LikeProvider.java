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
import static com.example.demo.config.BaseResponseStatus.DELETED_USER;

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
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<GetCollectionProductsRes> getProductRes = likeDao.getCollectionProducts(userIdx,collectionIdx,status);
            return getProductRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetLikesRes getLikes(int userIdx, String order, String status) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            GetLikesRes getLikesRes = likeDao.getLikes(userIdx,order,status);
            return getLikesRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isDeletedUser(int userIdx) throws BaseException {
        try {
            int result = likeDao.isDeletedUser(userIdx);
            System.out.println(result);
            return likeDao.isDeletedUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistLike(int userIdx,int productIdx) throws BaseException {
        try {
            return likeDao.isExistLike(userIdx,productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int isExistCanceledLike(int userIdx,int productIdx) throws BaseException {
        try {
            return likeDao.isExistCanceledLike(userIdx,productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int isDeletedCollections(int userIdx,int collectionIdx) throws BaseException {
        try {
            return likeDao.isDeletedCollections(userIdx,collectionIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistColloectionIdx(int collectionIdx) throws BaseException {
        try {
            return likeDao.isExistColloectionIdx(collectionIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
