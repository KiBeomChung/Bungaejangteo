package com.example.demo.src.review.model;

import lombok.*;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GetReviewRes {

    @NonNull private int reviewId;
    @NonNull private String imageUrl;
    @NonNull private int id; //구매자 id
    @NonNull private String storeName;
    @NonNull private int reviewScore;
    @NonNull private String reviewText;
    @NonNull private String name; //상품 명
    @NonNull private String createdAt;
    private GetCommentRes comment;
    private List<GetReviewImageRes> reviewImages;
}
