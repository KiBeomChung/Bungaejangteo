package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.address.model.Address;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class AddressService {

    private static String GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query=";
    private static String GEOCODE_USER_INFO = "KakaoAK 41bba28d7be73b43f6d5b691b2b9894c";

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AddressDao addressDao;
    private final AddressProvider addressProvider;
    private final JwtService jwtService;
    //private final KakaoUtil kakaoUtil;

    public AddressService(AddressDao addressDao, AddressProvider addressProvider, JwtService jwtService) {
        this.addressDao = addressDao;
        this.addressProvider = addressProvider;
        this.jwtService = jwtService;
    }

    public String createAddress(PostAddressReq postAddressReq, int userId) throws BaseException {

        String location = postAddressReq.getRealAddress();
        BigDecimal latitude = null;
        BigDecimal longitude = null;
        String roadNameAddress = null;
        String result = "";
        //String landNumAddress = null;
        //int searchCategory = 0;

        try {
            location = URLEncoder.encode(location, "UTF-8");

            URL url = new URL(GEOCODE_URL + location);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            String addressResult = "";

            while ((inputLine = in.readLine()) != null) {
                addressResult += inputLine;
            }
            in.close();

            //response 객체를 출력해보자
            System.out.println(addressResult);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(addressResult);

            JsonArray documents = element.getAsJsonObject().get("documents").getAsJsonArray();
            int total_count = element.getAsJsonObject().get("meta").getAsJsonObject().get("total_count").getAsInt(); // 검색된 문서 수

            if (total_count == 1) {
//                    searchCategory = 1;
                    latitude = documents.getAsJsonArray().get(0).getAsJsonObject().get("y").getAsBigDecimal();
                    longitude = documents.getAsJsonArray().get(0).getAsJsonObject().get("x").getAsBigDecimal();
                    roadNameAddress = documents.getAsJsonArray().get(0).getAsJsonObject().get("address_name").getAsString();
//                    landNumAddress = documents.getAsJsonArray().get(0).getAsJsonObject().get("address").getAsJsonObject().get("address_name").getAsString();
                }

        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

//        location = postAddressReq.getRealAddress();
//
//        try {
//            location = location = URLEncoder.encode(location, "UTF-8");
//
//            URL url = new URL(GEOCODE_URL + location);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
//            con.setRequestProperty("content-type", "application/json");
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//            con.setDefaultUseCaches(false);
//
//            Charset charset = Charset.forName("UTF-8");
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
//
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            String addressResult = "";
//
//            while ((inputLine = in.readLine()) != null) {
//                addressResult += inputLine;
//            }
//            in.close();
//
//            //response 객체를 출력해보자
//            System.out.println(addressResult);
//
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(addressResult);
//
//            JsonArray documents = element.getAsJsonObject().get("documents").getAsJsonArray();
//            int total_count = element.getAsJsonObject().get("meta").getAsJsonObject().get("total_count").getAsInt(); // 검색된 문서 수
//
//            if (total_count == 1) {
//                if(searchCategory == 0)
//                {
//                    latitude = documents.getAsJsonArray().get(0).getAsJsonObject().get("y").getAsBigDecimal();
//                    longitude = documents.getAsJsonArray().get(0).getAsJsonObject().get("x").getAsBigDecimal();
//                    roadNameAddress = documents.getAsJsonArray().get(0).getAsJsonObject().get("road_address_name").getAsString();
//                }
//            }
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        if(roadNameAddress == null){
//            throw new BaseException(INVALID_ADDRESS);
//        }

//        if(searchCategory == 1) {
            int affected = addressDao.createAddress(new Address(userId, postAddressReq.getNickName(), postAddressReq.getPhoneNum(), latitude, longitude, roadNameAddress, postAddressReq.getDetailAddress(), postAddressReq.getIsPrimaryAddress()));
//                    );
//        } else {
//            addressDao.createAddress(new Address(userId, postAddressReq.getNickName(), postAddressReq.getPhoneNum(), latitude, longitude, post roadNameAddress, postAddressReq.getDeatilAddress())
//                    );
//        }

        if(affected == 1) {
            result = "완료";
        }
        return result;
    }
}
