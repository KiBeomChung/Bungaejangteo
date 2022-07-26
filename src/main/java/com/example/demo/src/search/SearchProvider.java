package com.example.demo.src.search;

import com.example.demo.src.search.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.src.brand.model.GetBrandListRes;
import com.example.demo.src.search.SearchService;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    private final JwtService jwtService;

    @Autowired
    public SearchProvider(SearchDao searchDao, JwtService jwtService) {
        this.searchDao = searchDao;
        this.jwtService = jwtService;
    }

    public List<GetSearchWordRes> getSearchWord(int userIdx, String type) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<GetSearchWordRes> getBrandListRes = searchDao.getSearchWord(userIdx,type);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isCorrenctUser(int searchIdx) throws BaseException {
        try {
            return searchDao.isCorrenctUser(searchIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isExistSearchIdx(int searchIdx) throws BaseException {
        try {
            return searchDao.isExistSearchIdx(searchIdx);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isDeletedUser(int userIdx) throws BaseException {
        try {
            int result = searchDao.isDeletedUser(userIdx);
            System.out.println(result);
            return searchDao.isDeletedUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchBrandRes> getSearchBrandList(int userId, int brandId) throws BaseException {

        if(searchDao.isExistBrand(brandId) == 0){ //브랜드가 존재하지 않을 경우 -> 확인
            throw new BaseException(NOT_EXISTS_BRAND);
        }
        if(searchDao.checkBrandStatus(brandId) == 1) {   //브랜드가 삭제 되었을 경우 -> 확인
            throw new BaseException(NOT_AVALIABLE_BRAND_STATUS);
        }

        try {
            List<GetSearchBrandRes> getSearchBrandResList = searchDao.getSearchBrandList(userId, brandId);
            return getSearchBrandResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
