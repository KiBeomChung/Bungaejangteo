package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import java.util.List;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public List<GetProductRes> getRecommendProducts(int userIdx) throws BaseException {
        try {
            List<GetProductRes> getProductRes = productDao.getRecommendProducts(userIdx);
            return getProductRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getReport(int userIdx,int productIdx) throws BaseException {
        try {
            return productDao.getReport(userIdx,productIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetDetailProductRes getDetailProduct(int userIdx,int productIdx) throws BaseException {
        try {
            GetDetailProductRes getProductRes = productDao.getDetailProduct(userIdx,productIdx);
            return getProductRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

