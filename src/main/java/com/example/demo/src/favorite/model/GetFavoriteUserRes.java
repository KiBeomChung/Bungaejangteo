package com.example.demo.src.favorite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteUserRes {

    private List<GetFavoriteUserDetailRes> getFavoriteUserDetailResList;
}
