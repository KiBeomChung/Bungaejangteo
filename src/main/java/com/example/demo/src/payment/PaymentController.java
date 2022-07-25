package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.payment.model.GetPaymentUserInfoRes;
import com.example.demo.src.payment.model.GetProductInfoRes;
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

    @ResponseBody
    @GetMapping("/{productId}/product-info")
    public BaseResponse<GetProductInfoRes> getProductInfoRes(@PathVariable("productId") int productId) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            GetProductInfoRes getProductInfoRes = paymentProvider.getProductInfoRes(productId, userIdxByJwt);
            return new BaseResponse<>(getProductInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    @ResponseBody
//    @GetMapping("/{productId}/store-info")
//    public BaseResponse<GetPaymentUserInfoRes> getPaymentUserInfoRes(@PathVariable("productId") int productId) {
//        try {
//            int userIdxByJwt = jwtService.getUserIdx();
//
//            GetPaymentUserInfoRes getPaymentUserInfoRes = paymentProvider.getPaymentUserInfo(productId, userIdxByJwt);
//            return new BaseResponse<>(getPaymentUserInfoRes);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//    }
}