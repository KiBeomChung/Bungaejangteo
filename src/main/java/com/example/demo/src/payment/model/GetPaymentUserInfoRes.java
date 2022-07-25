package com.example.demo.src.payment.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GetPaymentUserInfoRes {

    private String price;
    private String tax;
    private String sum;
    private String usedPoint;
    private String totalPrice;

}
