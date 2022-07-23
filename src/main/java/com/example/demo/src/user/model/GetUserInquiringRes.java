package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInquiringRes {

    private String imageUrl;
    private String storeName;
    private String text;
    private String createdAt;
}
