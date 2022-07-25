package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAnotherUserStoreProductInfoRes {

    private int productId;
    private String imageUrl;
    private int price;
    private String name;
    private int like;
}
