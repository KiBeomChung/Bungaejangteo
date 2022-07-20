package com.example.demo.src.favorite;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.PostFavoriteStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
