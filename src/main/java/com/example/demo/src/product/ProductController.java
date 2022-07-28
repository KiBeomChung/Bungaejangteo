package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;


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

    @PostMapping("")
    public BaseResponse<Integer> createProduct(@RequestBody PostProductReq postProductReq) {

        if (postProductReq.getName() == null) {
            return new BaseResponse<>(POST_PRODUCT_EMPTY_PRODUCT_NAME);
        }

        if (postProductReq.getCategory() == null) {
            return new BaseResponse<>(POST_PRODUCT_EMPTY_CATEGORY);
        }

        if (!(postProductReq.getCategory() > 0 && postProductReq.getCategory() < 20 )) {
            return new BaseResponse<>( INVALIDT_CATEGORY_CODE_TYPE);
        }

        if (postProductReq.getPrice() == null) {
            return new BaseResponse<>(POST_PRODUCT_PRODUCT_EMPTY_PRICE);
        }

        if (postProductReq.getDescription() == null) {
            return new BaseResponse<>(POST_PRODUCT_EMPTY_DESCRIPTION);
        }

        if (postProductReq.getImages() == null) {
            return new BaseResponse<>(POST_PRODUCT_PRODUCT_EMPTY_IMAGES);
        }

        if (!(isRegexLangType(postProductReq.getCategory().toString())|| isRegexLangType(postProductReq.getPrice().toString()) )) {
            return new BaseResponse<>(INCORRECT_TYPEOF_PRODUCT_INT);
        }

        if (postProductReq.getLatitude() != null){
            if (!isRegexLatidue(String.valueOf(postProductReq.getLatitude()))) {
                return new BaseResponse<>(INCORRECT_SHAPEOF_LATITUDE);
            }

        }

        if (postProductReq.getLongitude() != null){
            if (!isRegexLogitude(String.valueOf(postProductReq.getLongitude()))) {
                return new BaseResponse<>(INCORRECT_SHAPEOF_LONGITUDE);
            }
        }


        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int productIdx = productService.createProduct(userIdxByJwt,postProductReq);
            return new BaseResponse<>(productIdx);


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 상세 정보 조회 API
     * [GET] /app/products/:productIdx
     * @return BaseResponse<GetDetailProductRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{productIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetDetailProductRes> getDetailProduct(@PathVariable("productIdx") Integer productIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            GetDetailProductRes getProductRes = productProvider.getDetailProduct(userIdxByJwt,productIdx);
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 특정 상품 조회시 해당 상품과 연관있는 상품 조회 API
     * @param productIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/{productIdx}/relation")
    public BaseResponse<List<GetRelatedProdcutRes>> getRelatedProduct(@PathVariable("productIdx") int productIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (productProvider.checkUserStatusByUserId(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetRelatedProdcutRes> getRelatedProdcutRes = productProvider.getRelatedProduct(userIdxByJwt, productIdx);
            return new BaseResponse<>(getRelatedProdcutRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *검색 - 검색어 엔터치기 전에 상품 검색
     * [GET] /app/products/search?searchword=
     * @return BaseResponse<List<String>>
     */
    @GetMapping("/search")
    public BaseResponse<List<String>> getProductSearchWord(@RequestParam(value = "searchword") String searchword) {
        if(searchword.equals("")){
            return new BaseResponse<>(EMPTY_SEARCHWORD);
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (productProvider.isDeletedUser(userIdxByJwt) == 1){
                throw new BaseException(DELETED_USER);
            }
            List<String> result = productProvider.getProductSearchWord(searchword);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *검색 - 검색어 엔터치기 전에 상품 검색
     * [GET] /app/products?searchword=
     * @return BaseResponse<List<GetProductRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetProductRes>> getProductFiltering(@RequestParam(value = "searchword") String searchword,@RequestParam(value = "category") Integer category
    ,@RequestParam(value = "order") String order,@RequestParam(value = "brand") String brand,@RequestParam(value = "minprice") Integer minprice
    ,@RequestParam(value = "maxprice") Integer maxprice,@RequestParam(value = "soldout") String soldout,@RequestParam(value = "deliveryfee") String deliveryfee
    ,@RequestParam(value = "status") String status) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (productProvider.isDeletedUser(userIdxByJwt) == 1){
                throw new BaseException(DELETED_USER);
            }
            FiteringPrameters fiteringPrameters = new FiteringPrameters(searchword ,category ,order ,brand ,minprice ,maxprice ,soldout, deliveryfee,status);
            List<GetProductRes> result = productProvider.getProductFiltering(userIdxByJwt,fiteringPrameters);

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
