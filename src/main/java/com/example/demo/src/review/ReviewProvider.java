package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReviewProvider {

    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public int checkUserStatusByUserId(int userId) throws BaseException {
        try {
            return reviewDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetReviewRes> getReviewRes(int id) throws BaseException {

        List<GetReviewRes> getReviewResList = reviewDao.getReviewList(id);

        int i = 0;
        for(GetReviewRes g : getReviewResList) {
            System.out.println(i++);
            if(reviewDao.checkComment(g.getReviewId()) == 1) {
                // 만일 해당 리뷰에 대한 코멘트가 있으면
                g.setComment(reviewDao.getReviewComment(g.getReviewId(), id));
            }
            if(reviewDao.checkImage(g.getReviewId()) != 0){
                g.setReviewImages(reviewDao.getReviewImages(g.getReviewId()));
            }
        }

        return getReviewResList;
    }


}
