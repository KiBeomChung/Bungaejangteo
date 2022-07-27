package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReportReq {

    private int reportCategory;
    private int reviewId;

    public PostReviewReportReq() {

    }
}
