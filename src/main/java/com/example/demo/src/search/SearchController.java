package com.example.demo.src.search;

import com.example.demo.src.search.model.*;
import com.example.demo.src.brand.model.getFollowBrandRes;
import com.example.demo.utils.JwtService;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/searchs")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;

    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService) {
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    /**
     *검색 - 최근 검색어/요즘 많이 찾는 검색어 조회 API
     * [GET] /app/searchs?type=
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetSearchWordRes>> getSearchWord(@RequestParam(value = "type") String type) {
        if(!(type.equals("recent") || type.equals("hot"))){
            return new BaseResponse<>(GET_SEARCHWORD_INVALID_TYPE);
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetSearchWordRes> result = searchProvider.getSearchWord(userIdxByJwt,type);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색어 전체 삭제 API
     * [DELETE] /app/searches
     * @return BaseResponse<String>
     */

    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<String> deleteAllSearchs() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            searchService.deleteAllSearchs(userIdxByJwt);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}