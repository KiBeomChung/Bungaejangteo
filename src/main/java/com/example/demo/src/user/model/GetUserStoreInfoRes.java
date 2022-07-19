package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserStoreInfoRes {

    private String imageUrl;
    private String storeName;
    private String shopUrl;
    private String contactTime;
    private String description;
    private String policy;
    private String precautions;
}
