package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.review.model.PostRegisterReviewReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;


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

    public String registerReview(int userId, PostRegisterReviewReq postRegisterReviewReq) throws BaseException {

        if (reviewDao.checkBuyerStatus(userId) == 1) {   // 내가 탈퇴
            throw new BaseException(DELETED_USER);
        }
        if (reviewDao.checkSellerStatus(postRegisterReviewReq.getProductId()) == 1) {
            throw new BaseException(NOT_AVALIABLE_SELLER_STATUS);    // 구매한 상점이 삭제
        }
        if (reviewDao.isAlreadyWriting(postRegisterReviewReq.getProductId()) != 0) {
            throw new BaseException(ALREADY_WRITING_REVIEW);  // 이미 작성했을 경우  -> 확인
        }
        if (reviewDao.checkCreatedAt(userId) > 30) {  // 리뷰 기한은 30일 이내로 작성하지 않았을 경우  -> 확인
            throw new BaseException(EXPIRED_REVIEW_WRITE);
        }

        try {
            String result = "";
            //먼저 리뷰 작성
            int lastInsertId = reviewDao.registerReview(postRegisterReviewReq);
            if (postRegisterReviewReq.getImageUrl().get(0) != null) {
                reviewDao.registerReviewImg(lastInsertId, postRegisterReviewReq.getImageUrl());
            }
            result = "리뷰 작성 완료 하였습니다.";
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
