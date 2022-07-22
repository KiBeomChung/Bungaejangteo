package com.example.demo.src.favorite.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor

public class GetFavoriteUserRes {

    @NonNull private int id;
    @NonNull private String imageUrl;
    @NonNull private String storeName;
    @NonNull private int productsNum;
    @NonNull private int followerNum;
    private List<GetFavoriteUserProductsDetailRes> getFavoriteUserProductsDetailResList;
    //private List<GetFavoriteUserDetailRes> getFavoriteUserDetailResList;
}
