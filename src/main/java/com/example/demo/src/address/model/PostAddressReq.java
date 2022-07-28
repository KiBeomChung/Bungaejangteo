package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAddressReq {

    private String nickName;
    private String realAddress;
    private String detailAddress;
    private String phoneNum;
    private int isPrimaryAddress;
    public PostAddressReq() {}
}
