package com.example.demo.src.like;

import com.example.demo.src.like.*;
import com.example.demo.src.like.LikeProvider;
import com.example.demo.config.*;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        try{
            int result = likeDao.createLike(userIdx,productIdx);
            if(result == 0){
                throw new BaseException(FAILED_TO_PRODUCT_LIKE);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }


}
