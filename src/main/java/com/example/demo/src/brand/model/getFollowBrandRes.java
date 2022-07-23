package com.example.demo.src.brand.model;


import com.example.demo.src.product.model.GetProductRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class getFollowBrandRes {
    private int brandIdx;
    private String koreanName;
    private String englishName;
    private int productNum;
    private boolean isFollowed;
    private String imageUrl;
    private List<GetProductRes> products;
}