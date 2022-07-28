package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMypageRes {
    private String name;
    private int isCertified;
    private String imageUrl;
    private Double ratings;
    private int likeNum;
    private int reviewNum;
    private int followerNum;
    private int followingNum;
}
