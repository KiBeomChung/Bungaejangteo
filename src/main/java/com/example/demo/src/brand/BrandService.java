package com.example.demo.src.brand;

import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.brand.*;
import com.example.demo.src.brand.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandDao brandDao;
    private final BrandProvider brandProvider;
    private final JwtService jwtService;

    @Autowired
    public BrandService(BrandDao brandDao, BrandProvider brandProvider, JwtService jwtService) {
        this.brandDao = brandDao;
        this.brandProvider = brandProvider;
        this.jwtService = jwtService;
    }

}