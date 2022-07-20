package com.example.demo.src.product;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.example.demo.config.BaseResponseStatus.*;


import java.util.List;


@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 홈 - 추천상품 조회 API
     * [GET] /products/recommend
     * @return BaseResponse<GetProductRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/recommend") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<List<GetProductRes>> getRecommendProducts() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetProductRes> getProductRes = productProvider.getRecommendProducts(userIdxByJwt);
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 상품 신고하기 API
     * [POST] /products/report/:productIdx
     *
     * @return BaseResponse<>
     */
    @ResponseBody
    @PostMapping("/report/{productIdx}")
    public BaseResponse<String> createReport(@PathVariable("productIdx") Integer productIdx,@RequestBody PostReportReq postReportReq) {

        if (productIdx == null) {
            return new BaseResponse<>(POST_REPORT_EMPTY_PRODUCT_IDX);
        }

        if (postReportReq.getReportType() == null) {
            return new BaseResponse<>(POST_REPORT_EMPTY_REPORT_TYPE);
        }

        if (postReportReq.getReportType() > 6 || postReportReq.getReportType() < 1) {
            return new BaseResponse<>(INVALIDT_REPORT_TYPE);
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            productService.createReport(userIdxByJwt,productIdx,postReportReq);
            return new BaseResponse<>(SUCCESS);


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
