package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.FiteringPrameters;
import com.example.demo.src.product.model.GetDetailProductRes;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.GetRelatedProdcutRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DELETED_USER;

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
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            return productDao.getReport(userIdx,productIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetDetailProductRes getDetailProduct(int userIdx,int productIdx) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            GetDetailProductRes getProductRes = productDao.getDetailProduct(userIdx,productIdx);
            return getProductRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isDeletedUser(int userIdx) throws BaseException {
        try {
            int result = productDao.isDeletedUser(userIdx);
            System.out.println(result);
            return productDao.isDeletedUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserStatusByUserId(int userId) throws BaseException {
        try {
            return productDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRelatedProdcutRes> getRelatedProduct(int userId, int productIdx) {

        List<GetRelatedProdcutRes> getRelatedProductRes = productDao.getRelatedProduct(userId, productIdx);
        return getRelatedProductRes;
    }

    public List<String> getProductSearchWord(String searchword) throws BaseException {
        try {
            List<String> getProductSearchWordRes = productDao.getProductSearchWord(searchword);
            return getProductSearchWordRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductRes> getProductFiltering(int userIdx, FiteringPrameters fiteringPrameters) throws BaseException {
        try {
            List<GetProductRes> getProductFilteringRes = productDao.getProductFiltering(userIdx,fiteringPrameters);
            return getProductFilteringRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

