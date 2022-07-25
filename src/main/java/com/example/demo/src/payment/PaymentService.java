package com.example.demo.src.payment;

import com.example.demo.src.payment.model.PostOrderInfoReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDao paymentDao;
    private final PaymentProvider paymentProvider;
    private final JwtService jwtService;

    public PaymentService(PaymentDao paymentDao, PaymentProvider paymentProvider, JwtService jwtService) {
        this.paymentDao = paymentDao;
        this.paymentProvider = paymentProvider;
        this.jwtService = jwtService;
    }

    public String storeOrderInfo(int userIdxByJwt, int productId, PostOrderInfoReq postOrderInfoReq) {


        String result = "";
        if(postOrderInfoReq.getDealCategory() == 0) {
            paymentDao.storeOrderInfo(userIdxByJwt, productId, postOrderInfoReq);
            result = "판매내역 저장완료하였습니다.";
            paymentDao.changeProductState(productId);
            paymentDao.storeBuySellInfo(userIdxByJwt, productId);
            // 판매자의 판매 횟수 +1
        } else if(postOrderInfoReq.getDealCategory() == 1) {
            paymentDao.storeOrderInfo(userIdxByJwt, productId, postOrderInfoReq); // 택배거래일 경우 메소드 만들어야함
            // 판매자의 판매 횟수 +1 , 상품 상태 변경 메소드 추가
            result = "판매내역 저장완료하였습니다.";
       }
        return result;
    }
}
