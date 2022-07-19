package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserStoreInfoReq {

    private String storeName;
    private String contactTime;
    private String description;
    private String policy;
    private String precautions;

    public PatchUserStoreInfoReq(){

    }
}
