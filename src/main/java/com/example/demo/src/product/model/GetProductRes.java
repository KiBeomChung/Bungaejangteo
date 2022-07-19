package com.example.demo.src.product.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {

    private int productIdx;
    private String imageUrl;
    private int like;
    private int price;
    private String name;
    private String region;
    private String elapsedTime;
    private int safePay;
    private int likeNum;
}
