package com.example.demo.src.payment.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class GetProductInfoRes {

    @NonNull private String imageUrl;
    @NonNull private int price;
    @NonNull private String name;
    @NonNull private int bungaePoint;
    private GetPaymentUserInfoRes getPaymentUserInfoRes;
    @NonNull private String payMethod;
    @NonNull private String isAgree;
}
