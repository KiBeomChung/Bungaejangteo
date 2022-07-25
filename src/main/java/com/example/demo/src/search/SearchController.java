package com.example.demo.src.search;

import com.example.demo.src.search.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;

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
     * [GET] /app/searchs/searchword?type=
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("/searchword")
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
     * [DELETE] /app/searchs
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

    /**
     * 특정 검색기록 삭제 API
     * [DELETE] /app/searches/:searchIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{searchIdx}")
    public BaseResponse<String> deleteSearch(@PathVariable("searchIdx") int searchIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            searchService.deleteSearch(userIdxByJwt,searchIdx);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("{userId}/{brandId}")
    public BaseResponse<List<GetSearchBrandRes>> getSearchBrandRes(@PathVariable("userId") int userId,
                                                                   @PathVariable("brandId") int brandId) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetSearchBrandRes> getSearchBrandResList = searchProvider.getSearchBrandList(userId, brandId);
            return new BaseResponse<>(getSearchBrandResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
