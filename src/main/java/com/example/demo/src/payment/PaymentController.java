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

    @ResponseBody
    @PostMapping("order/{productId}")
    public BaseResponse<String> storeOrderInfo(@PathVariable("productId") int productId,
                                               @RequestBody PostOrderInfoReq postOrderInfoReq) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            String result = paymentService.storeOrderInfo(userIdxByJwt, productId, postOrderInfoReq);
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}