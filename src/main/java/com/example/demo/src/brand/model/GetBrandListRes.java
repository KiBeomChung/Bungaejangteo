package com.example.demo.src.brand.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBrandListRes {
    private int brandIdx;
    private String koreanName;
    private String englishName;
    private int productNum;
    private boolean isFollowed;
    private String imageUrl;

}
