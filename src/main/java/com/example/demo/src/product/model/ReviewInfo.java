package com.example.demo.src.product.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ReviewInfo {
    private String name;
    private Double ratings;
    private String reviewText;
    private String elapsedTime;


}
