package com.example.demo.src.product.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class GetDetailProductRes {
    //상품 정보
    private int productIdx;
    private int price;
    private String name;
    private int like;
    private String region;
    private String elapsedTime;
    private int isDeliveryIncluded;
    private int count;
    private int isOld;
    private int isExchangeAvailable;
    private String description;
    private int isSafePayment;
    private int likeNum;
    private List<String> images;
    private List<String> tags;
    private String status;
    //판매자 상점 정보
    private storeInfo storeInfo;
    //리뷰 정보 2개
    private int reviewNum;
    private List<ReviewInfo> reviews;
    //문의 개수
    private int inqueryNum;

}
