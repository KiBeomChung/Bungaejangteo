package com.example.demo.src.favorite;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.GetFavoriteUserDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserProductsDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserRes;
import com.example.demo.src.favorite.model.PostFavoriteStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/favorites")
public class FavoriteController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FavoriteProvider favoriteProvider;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteProvider favoriteProvider, JwtService jwtService, FavoriteService favoriteService) {
        this.favoriteProvider = favoriteProvider;
        this.jwtService = jwtService;
        this.favoriteService = favoriteService;
    }

    /**
     * 상점 팔로우 추가
     *
     * @param followingId
     * @param followerId
     * @return
     */
    @ResponseBody
    @PostMapping("/add/{followingId}/{followerId}")
    public BaseResponse<PostFavoriteStoreRes> addFavorite(@PathVariable("followingId") int followingId,
                                                          @PathVariable("followerId") int followerId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (followerId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostFavoriteStoreRes postFavoriteStoreRes = favoriteService.addFavorite(followingId, followerId);
            return new BaseResponse<>(postFavoriteStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상점 팔로우 취소
     *
     * @param followingId
     * @param followerId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/delete/{followingId}/{followerId}")
    public BaseResponse<String> deleteFavorite(@PathVariable("followingId") int followingId,
                                               @PathVariable("followerId") int followerId) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (followerId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = favoriteService.deleteFavorite(followingId, followerId);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 브랜드 팔로우 API
     * @param userId
     * @param brandId
     * @return
     */
    @ResponseBody
    @PostMapping("brands/{userId}/{brandId}")
    public BaseResponse<String> addFollowBrand(@PathVariable("userId") int userId,
                                               @PathVariable("brandId") int brandId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = favoriteService.addFollowBrand(userId, brandId);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 브랜드 팔로우 취소 API
     * @param userId
     * @param brandId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/brands/{userId}/{brandId}")
    public BaseResponse<String> deleteFollowBrand(@PathVariable("userId") int userId,
                                                  @PathVariable("brandId") int brandId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = favoriteService.deleteFollowBrand(userId, brandId);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("{userId}/users")
    public BaseResponse<List<GetFavoriteUserRes>> getFavoriteUserList(@PathVariable("userId") int userId) {

        List<GetFavoriteUserRes> getFavoriteUserResList = favoriteProvider.getFavoriteUserResList(userId);
        return new BaseResponse<>(getFavoriteUserResList);
    }

//    /**
//     * 내가 팔로우 하는 상점들 정보 API (대표 상점 이미지, 상점 이미지, 상품 수, 팔로워 수)
//     * @param userId
//     * @return
//     */
//    @ResponseBody
//    @GetMapping("{userId}/users/store-info")
//    public BaseResponse<List<GetFavoriteUserDetailRes>> getFavoriteUserDetailRes (@PathVariable("userId") int userId) {
//
//        List<GetFavoriteUserDetailRes> getFavoriteUserDetailResList = favoriteProvider.getFavoriteUserDetailResList(userId);
//
//        return new BaseResponse<>(getFavoriteUserDetailResList);
//    }
//
//    @ResponseBody
//    @GetMapping("{userId}/users/store-product-info")
//    public BaseResponse<List<GetFavoriteUserProductsDetailRes>> getFavoriteUserProductsDetailRes(@PathVariable("userId") int userId) {
//
//        List<GetFavoriteUserProductsDetailRes> getFavoriteUserProductsDetailResList
//                = favoriteProvider.getFavoriteUserProductsDetailResList(userId);
//
//        return new BaseResponse<>(getFavoriteUserProductsDetailResList);
//    }
}
