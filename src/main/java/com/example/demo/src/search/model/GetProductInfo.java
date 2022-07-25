package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductInfo {
    private int productId;
    private String imageUrl;
    private int price;
    private int isLike;
    private String name;
    private int isSafePayment;
}
