package com.example.demo.src.review;

import com.example.demo.src.review.model.PostRegisterReviewReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    public String registerReview(int userId, PostRegisterReviewReq postRegisterReviewReq) {

        String result = "";
        //먼저 리뷰 작성
        int lastInsertId = reviewDao.registerReview(postRegisterReviewReq);
        if(postRegisterReviewReq.getImageUrl().get(0) != null) {
            reviewDao.registerReviewImg(lastInsertId, postRegisterReviewReq.getImageUrl());
        }
        result = "리뷰 작성 완료 하였습니다.";
        return result;
    }
}
