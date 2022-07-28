package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/reviews")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 구매상품에 대한 리뷰 작성
     *
     * @param postRegisterReviewReq
     * @return
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> registerReview(@RequestBody PostRegisterReviewReq postRegisterReviewReq) {

        if (postRegisterReviewReq.getReviewText() == null) {  //리뷰 텍스트가 null 일 경우
            return new BaseResponse<>(EMPTY_REVIEW_TEXT);
        }
        if (postRegisterReviewReq.getReviewText().length() == 0 || postRegisterReviewReq.getReviewText().length() > 1000) {
            return new BaseResponse<>(INVALID_REVIEW_TEXT_LENGTH);  // 리뷰 텍스트가 너무 길때
        }
        if (postRegisterReviewReq.getReviewScore() > 5 || postRegisterReviewReq.getReviewScore() < 0) {
            return new BaseResponse<>(INVALID_REVIEW_SCORE);     // 별점을 0~ 5 사이로 안줬을때
        }

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            String result = reviewService.registerReview(userIdxByJwt, postRegisterReviewReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 리뷰 조회
     *
     * @param id
     * @return BaseResponse<GetReviewRes>
     */
    @GetMapping("/{id}")
    @ResponseBody
    public BaseResponse<List<GetReviewRes>> getReviewResBaseResponse(@PathVariable("id") int id) throws BaseException {

        List<GetReviewRes> getReviewRes = reviewProvider.getReviewRes(id);
        return new BaseResponse<>(getReviewRes);
    }

    /**
     * 리뷰 수정 API
     *
     * @param patchModifyReviewReq
     * @return
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> modifyReview(@RequestBody PatchModifyReviewReq patchModifyReviewReq) {

        if (patchModifyReviewReq.getReviewText() == null) {
            return new BaseResponse<>(EMPTY_REVIEW_TEXT);
        }
        if (patchModifyReviewReq.getReviewText().length() == 0 || patchModifyReviewReq.getReviewText().length() > 1000) {
            return new BaseResponse<>(INVALID_REVIEW_TEXT_LENGTH);  // 리뷰 텍스트가 너무 길때
        }
        if (patchModifyReviewReq.getReviewScore() > 5 || patchModifyReviewReq.getReviewScore() < 0) {
            return new BaseResponse<>(INVALID_REVIEW_SCORE);     // 별점을 0~ 5 사이로 안줬을때
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (reviewProvider.checkUserStatusByUserId(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }

            String result = reviewService.modifyReview(userIdxByJwt, patchModifyReviewReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 리뷰 삭제 API
     *
     * @param deleteReviewReq
     * @return
     */
    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<String> deleteReview(@RequestBody DeleteReviewReq deleteReviewReq) {
        // buy, sell 테이블의 리뷰 수정, 리뷰테이블 해당 행 삭제, 리뷰 이미지 테이블의 해당 행 삭제

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (reviewProvider.checkUserStatusByUserId(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }

            String result = reviewService.deleteReview(userIdxByJwt, deleteReviewReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 신고 API
     * @param postReviewReportReq
     * @return
     */
    @ResponseBody
    @PostMapping("reports")
    public BaseResponse<String> reportReview(@RequestBody PostReviewReportReq postReviewReportReq) {

//        if(postReviewReportReq.getReportCategory() != 0 || postReviewReportReq.getReportCategory() != 1 || postReviewReportReq.getReportCategory() !=2) {
//            return new BaseResponse<>(NOT_PROPER_CATEGORY);
//        }

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (reviewProvider.checkUserStatusByUserId(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }
            String result = reviewService.reportReview(userIdxByJwt, postReviewReportReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 리뷰 댓글 작성
     * @param postRegisterCommentReq
     * @return
     */
    @ResponseBody
    @PostMapping("comments")
    public BaseResponse<String> registerComment(@RequestBody PostRegisterCommentReq postRegisterCommentReq) {

        //댓글 텍스트가 없을 경우
        if(postRegisterCommentReq.getCommentText() == null) {
            return new BaseResponse<>(EMPTY_COMMENT_TEXT);
        }
        if(postRegisterCommentReq.getCommentText().length() > 100) {
            return new BaseResponse<>(INVALID_COMMENT_LENGTH);
        }

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (reviewProvider.checkUserStatusByUserId(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }
            String result = reviewService.registerComment(userIdxByJwt, postRegisterCommentReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
