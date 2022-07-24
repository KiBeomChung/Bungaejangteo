package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAnotherUserStoreInfoRes {

    private String imageUrl;
    private String storeName;
    private double reviewAvg;
    private int reviewNum;
    private int openDate;
    private int productNum;
    private int reviewNum2;
    private int followNum;
    private int followingNum;
    private int isCertified;
    private int sellNum;
    private String contactTime;
    private String description;
    private String policy;
    private List<GetAnotherUserStoreProductInfoRes> getAnotherUserStoreProductInfoResList;
    private int isFollow;
    //택배 내역 추가 해야함 -> 결제 만들고
}
