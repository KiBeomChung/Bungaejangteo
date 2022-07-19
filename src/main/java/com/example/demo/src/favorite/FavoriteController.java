package com.example.demo.src.favorite;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.PostFavoriteStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PostMapping("/{followingId}/{followerId}")
    public BaseResponse<PostFavoriteStoreRes> addFavorite(@PathVariable("followingId") int followingId,
                                                          @PathVariable("followerId") int followerId) {

        PostFavoriteStoreRes postFavoriteStoreRes = favoriteService.addFavorite(followingId, followerId);
        return new BaseResponse<>(postFavoriteStoreRes);

    }
}
