package com.example.demo.src.payment.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class GetPaymentUserInfoRes {

    @NonNull private int bungaePoint;
    private String price;
    private String tax;
    private String sum;
    private String usedPoint;
    private String totalPrice;
    @NonNull private String payMethod;
    @NonNull private String isAgree;
}
