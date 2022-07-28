package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;


@Service
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;


    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;

    }

    public void createReport(int userIdx,Integer productIdx,PostReportReq postReportReq) throws BaseException {
        if (productProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if (productProvider.getReport(userIdx,productIdx) == 1){
            throw new BaseException(POST_REPORT_EXIST_REPORT);
        }
        try {
            productDao.createReport(userIdx,productIdx,postReportReq);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional
    public int createProduct(int userIdx, PostProductReq postProductReq) throws BaseException {
        if (productProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            int productIdx = productDao.createProduct(userIdx,postProductReq);
            productDao.createProductImages(productIdx,postProductReq.getImages());
            if (postProductReq.getTags() != null){
                productDao.createProductTags(productIdx,postProductReq.getTags());
            }
            return productIdx;

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int updateRecentProducts(int userIdx,int productIdx) throws BaseException {
        try {
            return productDao.updateRecentProducts(userIdx,productIdx);

        } catch (Exception exception) {

            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int createRecentProducts(int userIdx,int productIdx) throws BaseException {
        try {
            return productDao.createRecentProducts(userIdx,productIdx);

        } catch (Exception exception) {

            throw new BaseException(DATABASE_ERROR);
        }

    }
}

