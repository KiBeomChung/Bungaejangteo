package com.example.demo.src.payment;

import com.example.demo.config.BaseException;
import com.example.demo.src.payment.model.PostOrderInfoReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

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

    public int checkUserStatus(int userId) throws BaseException {
        try {
            return paymentDao.checkUserStatus(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String storeOrderInfo(int userIdxByJwt, int productId, PostOrderInfoReq postOrderInfoReq) throws BaseException {

        String result = "";
        if (postOrderInfoReq.getDealCategory() == 0) {
            int isStored = paymentDao.storeOrderInfo(userIdxByJwt, productId, postOrderInfoReq);

            if(paymentDao.duplicatedOrder(userIdxByJwt, productId) == 1) {
                throw new BaseException(ALREADY_EXIST_ORDER);// 이미 결제내역이 존재하는 경우
            }
            if(paymentDao.checkSellerStatus(productId) == 1) {
                throw new BaseException(NOT_AVALIABLE_SELLER_STATUS);  // 구매 불가능한 상점일 경우
            }
            if(paymentDao.checkProductStatus(productId) == 1) {
                throw new BaseException(PRODUCT_HAS_REPORTS);  // 상품이 신고당했을 경우
            }
            if(paymentDao.checkBuyerStatus(userIdxByJwt) == 1) {
                throw new BaseException(NOT_AVALIABLE_BUYER_STATUS);  // 구매자가 사용 불가능한 경우
            }
            if(paymentDao.isAlreadySoldOut(productId) == 1) {
                throw new BaseException(ALREADY_SOLD_OUT_PRODUCT);   // 상품이 이미 팔렸을 경우
            }
            if(paymentDao.checkBungaePoint(userIdxByJwt, postOrderInfoReq.getUsingBungaePoint()) == 1) {
                throw new BaseException(USED_BUNGAE_POINTS_WRONG);    // 자신이 가지고 있는 번개포인트 보다 더 사용했을 경우
            }

            if (isStored == 1) {
                result = "판매내역 저장완료하였습니다.";
                paymentDao.changeProductState(productId); // 상품 상태 변경 메소드
                // 변경에 실팼을 경우
                paymentDao.storeBuySellInfo(userIdxByJwt, productId); // 판매, 구매 테이블에 기록
                // 기록에 실패했을 경우
                if(postOrderInfoReq.getUsingBungaePoint() != 0) {
                    paymentDao.updateUserBungaePoint(userIdxByJwt, postOrderInfoReq.getUsingBungaePoint());
                }
            }

        } else if (postOrderInfoReq.getDealCategory() == 1) {
            //paymentDao.storeOrderInfo(userIdxByJwt, productId, postOrderInfoReq); // 택배거래일 경우 메소드 만들어야함
            // 판매자의 판매 횟수 +1 , 상품 상태 변경 메소드 추가
            result = "택배거래 API가 필요합니다. ";
        }
        return result;
    }

    public boolean checkPayMethod(String payMethod) {
        if (payMethod.equals("번개장터 간편결제")) return true;
        else if (payMethod.equals("신용/체크카드")) return true;
        else if (payMethod.equals("카카오페이")) return true;
        else if (payMethod.equals("토스")) return true;
        else if (payMethod.equals("간편계좌결제")) return true;
        else if (payMethod.equals("휴대폰결제")) return true;
        else return false;
    }
}
