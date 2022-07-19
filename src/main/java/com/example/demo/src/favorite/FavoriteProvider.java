package com.example.demo.src.favorite;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FavoriteDao favoirteDao;
    private final JwtService jwtService;

    @Autowired
    public FavoriteProvider(FavoriteDao favoirteDao, JwtService jwtService) {
        this.favoirteDao = favoirteDao;
        this.jwtService = jwtService;
    }
}
