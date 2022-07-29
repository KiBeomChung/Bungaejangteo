package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final LikeProvider likeProvider;
    private final JwtService jwtService;


    @Autowired
    public LikeService(LikeDao likeDao, LikeProvider likeProvider, JwtService jwtService) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
        this.jwtService = jwtService;

    }

    public void createLike(int userIdx,int productIdx) throws BaseException {
        if (likeProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if (likeProvider.isExistLike(userIdx,productIdx) == 1){
            throw new BaseException(ALREADY_LIKED_PRODUCT);
        }

        try{
            int result = likeDao.createLike(userIdx,productIdx);
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public void cancelLike(int userIdx,int productIdx) throws BaseException {
        if (likeProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if (likeProvider.isExistCanceledLike(userIdx,productIdx) == 1){
            throw new BaseException(ALREADY_CANCELED_LIKE);
        }
        try{
            int result = likeDao.cancelLike(userIdx, productIdx);
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public void createCollection(int userIdx,String collectionName) throws BaseException {
        if (likeProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try{
            int result = likeDao.createCollection(userIdx,collectionName);
            if(result == 0){
                throw new BaseException(FAILED_TO_CREATE_COLLECTION);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public void updateCollection(int userIdx,int collectionIdx,String collectionName) throws BaseException {
        if (likeProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if (likeProvider.isExistColloectionIdx(collectionIdx) == 0){
            throw new BaseException(NOT_EXIST_COLLECTION_IDX);
        }
        try{
            int result = likeDao.updateCollection(collectionIdx,collectionName);
            if(result == 0){
                throw new BaseException(FAILED_TO_UPDATE_COLLECTION);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }
    public void createCollectionProduct(int userIdx,int collectionIdx, List<Integer> productIdxList) throws BaseException {
        if (likeProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if (likeProvider.isExistColloectionIdx(collectionIdx) == 0){
            throw new BaseException(NOT_EXIST_COLLECTION_IDX);
        }
        try{
            int result = likeDao.createCollectionProduct(userIdx,collectionIdx,productIdxList);
            if(result == 0){
                throw new BaseException(FAILED_TO_CREATE_COLLECTION_PRODUCT);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public void deleteCollection(int userIdx,int collectionIdx) throws BaseException {
        if (likeProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if (likeProvider.isDeletedCollections(userIdx,collectionIdx) == 1){
            throw new BaseException(ALREADY_DELETED_COLLECTION);
        }
        if (likeProvider.isExistColloectionIdx(collectionIdx) == 0){
            throw new BaseException(NOT_EXIST_COLLECTION_IDX);
        }

        try{
            int result = likeDao.deleteCollection(collectionIdx);

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

}
