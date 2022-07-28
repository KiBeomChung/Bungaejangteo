package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Address {

    private int userId;
    private String nickName;
    private String phoneNum;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String detailAddress;
    private int isPrimaryAddress;
//    private String landNumAddress;
}
