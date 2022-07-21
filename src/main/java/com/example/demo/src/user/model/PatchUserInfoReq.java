package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserInfoReq {

    private String gender;
    private String birth;
    private String phoneNum;

    public PatchUserInfoReq() {
    }
}
