package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public List<GetReviewRes> getReviewRes(int id) throws BaseException {

        List<GetReviewRes> getReviewResList = reviewDao.getReviewList(id);
        return getReviewResList;
    }

//    public List<GetDateRes> getDateRes (int id, Timestamp dateNow){
//
//         List<GetDateRes> getCreatedDate = reviewDao.getCreatedDate(id); //디비에 저장된 시간을 가져옴

//         Timestamp getDate = getCreatedDate;

//         System.out.println("getDate :" + getDate);
//         System.out.println("현재 시간 :" + dateNow);

//         return getCreatedDate;

    }

//}

