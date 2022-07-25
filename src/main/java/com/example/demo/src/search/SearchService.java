package com.example.demo.src.search;

import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.search.*;
//import com.example.demo.src.search.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    private final SearchProvider searchProvider;
    private final JwtService jwtService;

    @Autowired
    public SearchService(SearchDao searchDao, SearchProvider searchProvider, JwtService jwtService) {
        this.searchDao = searchDao;
        this.searchProvider = searchProvider;
        this.jwtService = jwtService;
    }

    public void deleteAllSearchs(int userIdx) throws BaseException {
        if (searchProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try{
            int result = searchDao.deleteAllSearchs(userIdx);
            System.out.println(result);
            if(result == 0){
                throw new BaseException(FAILED_TO_DELETE_ALL_SEARCHES);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public void deleteSearch(int userIdx, int searchIdx) throws BaseException {
        if (searchProvider.isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        if(searchProvider.isExistSearchIdx(searchIdx) != 1){
            throw new BaseException(NOT_EXISTS_SEARCH_IDX);
        }
        if(searchProvider.isCorrenctUser(searchIdx) != userIdx){
            throw new BaseException(INCORRENT_USER_OF_SEARCHIDX);
        }
        try{
            int result = searchDao.deleteSearch(userIdx,searchIdx);
            if(result == 0){
                throw new BaseException(FAILED_TO_DELETE_ALL_SEARCHES);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);

        }
    }
}
