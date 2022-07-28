package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FiteringPrameters {

    private final String searchword;
    private final Integer category;
    private final String order;
    private final String brand;
    private final Integer minprice;
    private final Integer maxprice;
    private final String soldout;
    private final String deliveryfee;
    private final String status;


    public FiteringPrameters(String searchword ,int category ,String order ,String brand ,int minprice ,int maxprice ,String soldout, String deliveryfee,String status) {
        this.searchword = searchword;
       this.category = category;
       this.order = order;
       this.brand = brand;
       this.minprice = minprice;
       this.maxprice = maxprice;
       this.soldout = soldout;
       this.deliveryfee = deliveryfee;
       this.status = status;

    }
}
