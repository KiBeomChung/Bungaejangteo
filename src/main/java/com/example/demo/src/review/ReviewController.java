package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PostRegisterReviewReq;
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
     * @param postRegisterReviewReq
     * @return
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> registerReview(@RequestBody PostRegisterReviewReq postRegisterReviewReq) {

        if(postRegisterReviewReq.getReviewText() == null) {  //리뷰 텍스트가 null 일 경우
            return new BaseResponse<>(EMPTY_REVIEW_TEXT);
        }
        if(postRegisterReviewReq.getReviewText().length() == 0 || postRegisterReviewReq.getReviewText().length() > 1000){
            return new BaseResponse<>(INVALID_REVIEW_TEXT_LENGTH);  // 리뷰 텍스트가 너무 길때
        }
        if(postRegisterReviewReq.getReviewScore() > 5 || postRegisterReviewReq.getReviewScore() < 0) {
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

        Timestamp dateNow = new Timestamp(System.currentTimeMillis());

        List<GetReviewRes> getReviewRes = reviewProvider.getReviewRes(id);
        return new BaseResponse<>(getReviewRes);
    }
}
