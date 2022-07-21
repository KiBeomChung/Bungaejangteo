package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCollectionProductsRes {

    private int productIdx;
    private String productImageUrl;
    private String productName;
    private int price;
    private int storeIdx;
    private String storeImageUrl;
    private String storeName;
    private int safePay;
    private String status;

}