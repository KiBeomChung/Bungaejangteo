package com.example.demo.src.address;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.DELETED_USER;

@RestController
@RequestMapping("/app/addresses")
public class AddressController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AddressService addressService;
    private final AddressProvider addressProvider;
    private final JwtService jwtService;

    public AddressController(AddressService addressService, AddressProvider addressProvider, JwtService jwtService) {
        this.addressService = addressService;
        this.addressProvider = addressProvider;
        this.jwtService = jwtService;
    }


    /**
     * 유저의 배송지 추가
     * @param postAddressReq
     * @return
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createAddress(@RequestBody PostAddressReq postAddressReq) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (addressProvider.checkUserStatusByUserId(userIdxByJwt) == 1) {
                return new BaseResponse<>(DELETED_USER);
            }
            String result = addressService.createAddress(postAddressReq, userIdxByJwt);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

