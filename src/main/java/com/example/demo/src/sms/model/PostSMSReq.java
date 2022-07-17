package com.example.demo.src.sms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSMSReq {

    private String phoneNum;
    private String name;

}
