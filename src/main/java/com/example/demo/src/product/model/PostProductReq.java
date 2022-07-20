package com.example.demo.src.product.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductReq {
    private String name;
    private Integer category;
    private Integer price;
    private int isDeliveryIncluded;
    private int count;
    private int isOld;
    private int isExchangeAvailable;
    private int isSafePayment;
    private String region;
    private Double latitude;
    private Double longitude;
    private String description;
    private List<String> images;
    private List<String> tags;

}
