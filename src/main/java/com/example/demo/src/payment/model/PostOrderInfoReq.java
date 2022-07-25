package com.example.demo.src.payment.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PostOrderInfoReq {

    @NonNull private int dealCategory;
    @NonNull private String productName;
    private String dealName;
    private String address;
    private String deliverRequire;
    @NonNull private String finalPrice;
    @NonNull private int bungaePay;
    @NonNull private String payMethod;
    @NonNull private String isAgree;

    public PostOrderInfoReq() {}
}
