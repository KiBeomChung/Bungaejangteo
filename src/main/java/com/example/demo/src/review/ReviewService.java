package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.PostReportReq;
import com.example.demo.src.review.model.DeleteReviewReq;
import com.example.demo.src.review.model.PatchModifyReviewReq;
import com.example.demo.src.review.model.PostRegisterReviewReq;
import com.example.demo.src.review.model.PostReviewReportReq;
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

                reviewDao.updateBuySell(userId, lastInsertId, postRegisterReviewReq); // buy, sell 테이블에 정보 저장
            }
            result = "리뷰 작성 완료 하였습니다.";
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String modifyReview(int userId, PatchModifyReviewReq patchModifyReviewReq) throws BaseException {

        if (reviewDao.isExistReview(patchModifyReviewReq.getReviewId()) == 0) {
            throw new BaseException(NOT_EXIST_REVIEW);   //해당 리뷰가 없을 경우
        }
        if (reviewDao.checkBuyerStatus(userId) == 1) {   // 내가 탈퇴
            throw new BaseException(DELETED_USER);
        }
        if (reviewDao.checkCreatedAt(userId) > 30) {  // 리뷰 기한은 30일 이내로 작성하지 않았을 경우  -> 확인
            throw new BaseException(EXPIRED_REVIEW_WRITE);
        }
        int result = 0;
        String res = "";
        try {
            result = reviewDao.modifyReview(patchModifyReviewReq);

            if (result == 1) {
                res = "수정 완료 하였습니다.";
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
        return res;
    }

    public String deleteReview(int userId, DeleteReviewReq deleteReviewReq) throws BaseException {


        if (reviewDao.isExistReview(deleteReviewReq.getReviewId()) == 0) {
            throw new BaseException(NOT_EXIST_REVIEW_DELETE);   //해당 리뷰가 없을 경우
        }
        if (reviewDao.checkBuyerStatus(userId) == 1) {   // 내가 탈퇴
            throw new BaseException(DELETED_USER);
        }

        String result = "";
        try {
            reviewDao.deleteReviewImage(deleteReviewReq);
            if (reviewDao.updateBuySell(deleteReviewReq) == 2){
                if (reviewDao.deleteReviewReport(deleteReviewReq.getReviewId()) == 1 && reviewDao.deleteReview(deleteReviewReq) == 1) {
                    result = "리뷰 삭제를 완료했습니다.";
                    return result;
                }
                throw new BaseException(DATABASE_ERROR);
            }
            throw new BaseException(DATABASE_ERROR);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String reportReview(int userId, PostReviewReportReq postReviewReportReq) throws BaseException {

        if (reviewDao.checkReportAvailable(userId, postReviewReportReq.getReviewId()) != 1) {   // 내가 해당 리뷰의 주인이 아닐경우
            throw new BaseException(NOT_REPORT_USER);
        }
        if (reviewDao.isExistReview(postReviewReportReq.getReviewId()) == 0) {
            throw new BaseException(NOT_EXIST_REVIEW_DELETE);   //해당 리뷰가 없을 경우
        }

        if (reviewDao.isAlreadyReport(postReviewReportReq.getReviewId()) == 1) {
            throw new BaseException(ALREADY_REPORT_REVIEW);   //이미 신고된 리뷰
        }


        String result = "";

        try {
            int res = reviewDao.reportReview(postReviewReportReq);
            if (res == 1) {
                result = "해당 리뷰 신고를 완료했습니다.";
                return result;
            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
