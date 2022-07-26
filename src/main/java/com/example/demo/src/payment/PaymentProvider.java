package com.example.demo.src.payment;

import com.example.demo.src.payment.model.GetPaymentUserInfoRes;
import com.example.demo.src.payment.model.GetProductInfoRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentProvider {

    private final PaymentDao paymentDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PaymentProvider(PaymentDao paymentDao, JwtService jwtService) {
        this.paymentDao = paymentDao;
        this.jwtService = jwtService;
    }

    public GetProductInfoRes getProductInfoRes(int productId, int userIdxByJwt) {

        GetProductInfoRes getProductInfo = paymentDao.getProductInfo(productId, userIdxByJwt);

        String ProductPrice = paymentDao.getProductPrice(productId);
        String ProductTax = paymentDao.getProductTax(productId);
        String sum = paymentDao.getSum(productId);
        String usedPoint = paymentDao.getUsedPoint(userIdxByJwt);
        String finalSum = paymentDao.getFinalSum(productId, userIdxByJwt);

        GetPaymentUserInfoRes getPaymentUserInfo = new GetPaymentUserInfoRes(ProductPrice, ProductTax, sum, usedPoint, finalSum);

        getProductInfo.setGetPaymentUserInfoRes(getPaymentUserInfo);

        return getProductInfo;
    }

}
