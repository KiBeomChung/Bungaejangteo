package com.example.demo.src.like;
import com.example.demo.config.*;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/app/likes")
public class LikeController {

        final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Autowired
        private final LikeProvider likeProvider;
        @Autowired
        private final LikeService likeService;
        @Autowired
        private final JwtService jwtService;

        public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
            this.likeProvider = likeProvider;
            this.likeService = likeService;
            this.jwtService = jwtService;
        }

        /**
         * 상품 찜 등록 API
         * [POST] /likes/:productIdx
         * @return BaseResponse<String>
         */
        // Body
        @ResponseBody
        @PostMapping("/{productIdx}")
        public BaseResponse<String> createLike(@PathVariable("productIdx") int productIdx) {
            try {
                int userIdxByJwt = jwtService.getUserIdx();

                likeService.createLike(userIdxByJwt,productIdx);
                return new BaseResponse<>(SUCCESS);

            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 상품 찜 취소 API
         * [PATCH] /likes/:productIdx
         * @return BaseResponse<String>
         */
        // Body
        @ResponseBody
        @PatchMapping("/{productIdx}")
        public BaseResponse<String> cancelLike(@PathVariable("productIdx") int productIdx) {
            try {
                //jwt에서 idx 추출.
                int userIdxByJwt = jwtService.getUserIdx();

                likeService.cancelLike(userIdxByJwt,productIdx);
                return new BaseResponse<>(SUCCESS);

            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }






}

