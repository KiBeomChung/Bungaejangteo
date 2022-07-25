package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchBrandRes {

    private String imageUrl;
    private String name;
    private String englishName;
    private int isFollow;
    private List<GetProductInfo> getProductInfo;
}

