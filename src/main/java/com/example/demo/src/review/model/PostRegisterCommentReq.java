package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostRegisterCommentReq {

    private String commentText;
    private int reviewId;
    private int id;

    public PostRegisterCommentReq () {}
}
