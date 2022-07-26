package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.GetPaymentUserInfoRes;
import com.example.demo.src.payment.model.GetProductInfoRes;
import com.example.demo.src.payment.model.PostOrderInfoReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/payment")
public class PaymentController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PaymentProvider paymentProvider;
    @Autowired
    private final PaymentService paymentService;
    @Autowired
    private final JwtService jwtService;

    public PaymentController(PaymentProvider paymentProvider, PaymentService paymentService, JwtService jwtService) {
        this.paymentProvider = paymentProvider;
        this.paymentService = paymentService;
        this.jwtService = jwtService;
    }

    /**
     * 결제 기본 정보 조회 API
     * @param productId
     * @return
     */
    @ResponseBody
    @GetMapping("/{productId}")
    public BaseResponse<GetProductInfoRes> getProductInfoRes(@PathVariable("productId") int productId) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            GetProductInfoRes getProductInfoRes = paymentProvider.getProductInfoRes(productId, userIdxByJwt);
            return new BaseResponse<>(getProductInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 결제 완료 후 결제 정보 저장 API
     * @param productId
     * @param postOrderInfoReq
     * @return
     */
    @ResponseBody
    @PostMapping("order/{productId}")
    public BaseResponse<String> storeOrderInfo(@PathVariable("productId") int productId,
                                               @RequestBody PostOrderInfoReq postOrderInfoReq) throws BaseException {

        if(postOrderInfoReq.getDealCategory() > 1  || postOrderInfoReq.getDealCategory() < 0) {
            return new BaseResponse<>(POST_DEAL_CATEGORY_IS_EMPTY);    // dealCategory 값이 0 or 1 이 아닐경우
        }
        if(postOrderInfoReq.getProductName() == null) {
            return new BaseResponse<>(POST_EMPTY_PRODUCT_NAME);   // 상품이름이 null 인 경우
        }
        if(postOrderInfoReq.getFinalPrice() == null) {
            return new BaseResponse<>(POST_EMPTY_PRICE);     // 상품 가격이 null 인 경우
        }
        // 상품가격의 정규식 확인

        if(postOrderInfoReq.getPayMethod() == null) {
            return new BaseResponse<>(POST_EMPTY_PAY_METHOD);    // payMethod값이 null 인경우
        }
        if(!paymentService.checkPayMethod(postOrderInfoReq.getPayMethod())) {
            return new BaseResponse<>(POST_WRONG_PAY_METHOD);    // payMethod가 지정된 수단 이외의 경우
        }
        if(postOrderInfoReq.getIsAgree().equals("false")) {
            return new BaseResponse<>(POST_DISAGREE_PAYMENT);   // isAgree 가 false 인 경우
        }

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(paymentService.checkUserStatus(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }

            String result = paymentService.storeOrderInfo(userIdxByJwt, productId, postOrderInfoReq);
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}