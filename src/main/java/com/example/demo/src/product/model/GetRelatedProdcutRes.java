package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRelatedProdcutRes {

    private int productId;
    private String imageUrl;
    private int price;
    private String name;
    private int isLike;
    private int isSafePayment;
    private String tag;
}
