package com.example.demo.src.favorite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteUserDetailRes {

    private String imageUrl;
    private String storeName;
    private int productsNum;
    private int followerNum;
    //private List<GetFavoriteUserProductsDetailRes> getFavoriteUserProductsDetailResList;

//    public GetFavoriteUserDetailRes(String imageUrl, String storeName, int productsNum, int followerNum, Object getFavoriteUserProductsDetailResList, Class<List> listClass) {
//    }

//        public GetFavoriteUserDetailRes(String imageUrl, String storeName, int productsNum, int followerNum) {
//    }
}
