package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class storeInfo {
    private int storeIdx;
    private String storeImageUrl;
    private String storeName;
    private Double ratings;
    private int followerNum;
    private boolean followed;
    private int productNum;
    private List<GetProductRes> products; //최대6개
}
