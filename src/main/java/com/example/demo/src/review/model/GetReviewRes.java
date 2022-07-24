package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {

    private String imageUrl;
    private int id; //구매자 id
    private String storeName;
    private int reviewScore;
    private String bungaePay;
    private String reviewText;
    private String name; //상품 명
    private String createdAt;
}
