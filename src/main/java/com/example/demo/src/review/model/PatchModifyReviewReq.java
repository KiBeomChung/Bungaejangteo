package com.example.demo.src.review.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchModifyReviewReq {

    private int reviewId;
    private String reviewText;
    private int reviewScore;

    public PatchModifyReviewReq() {}
}
