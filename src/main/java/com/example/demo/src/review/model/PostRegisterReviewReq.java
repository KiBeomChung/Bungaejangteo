package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostRegisterReviewReq {

    private String reviewText;
    private int reviewScore;
    private int productId;
    private List<String> imageUrl;

    public PostRegisterReviewReq() {}
}
