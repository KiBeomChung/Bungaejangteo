package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchStoreRes {
    int storeIdx;
    String ImageUrl;
    String name;
    int followerNum;
    int productNum;

}
