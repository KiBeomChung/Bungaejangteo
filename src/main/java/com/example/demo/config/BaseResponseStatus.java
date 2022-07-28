package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    EMPTY_SEARCHWORD(false, 2011, "검색어를 입력해주세요"),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    EMPTY_PHONENUM(false,2018,"휴대폰 번호를 입력해주세요."),
    INCORRECT_TYPEOF_PHONENUM(false,2019,"휴대폰 번호는 숫자로만 구성되어야 합니다."),
    INCORRECT_SHAPEOF_PHONENUM(false,2020,"정확한 휴대폰 번호를 입력해주세요"),
    POST_USERS_EMPTY_NAME(false,2021,"이름을 입력해주세요"),
    POST_USERS_EMPTY_STORENAME(false,2022,"상점 이름을 입력해주세요"),
    POST_USERS_LONG_STORENAME(false,2023,"상점 이름은 10자 이내로 설정해주세요"),
    INCORRECT_TYPEOF_STORENAME(false,2024,"상점 이름은 한국어,영어,숫자만 사용가능합니다"),
    POST_USERS_LONG_INQUIRING(false,2025, "문의 내용이 너무 깁니다. 100자 이내로 작성하세요."),
    POST_USERS_EMPTY_INQUIRING(false, 2026, "문의하시려면 문의 내용을 작성해주세요."),
    EMPTY_ACCESS_TOKEN(false, 2027, "access Token을 입력해주세요"),
    //EMPTY_ACCESS_TOKEN(false, 2028, "access Token을 입력해주세요"),

    // [PATCH] /users
    PATCH_USERS_EMPTY_STORENAME(false,2050,"상점 이름을 입력해주세요."),
    PATCH_USERS_LONG_STORENAME(false, 2051, "상점 이름은 10자 이내로 수정해주세요."),
    PATCH_USERS_EMPTY_SHOPURL(false, 2052, "등록할 url을 입력해주세요."),
    PATCH_USERS_LONG_SHOPURL(false,2053, "url은 50자 이내로 입력해주세요"),
    PATCH_USERS_EMPTY_CONTACTTIME(false, 2054, "연락 가능 시간을 입력하세요."),
    PATCH_USERS_EMPTY_DESCRIPTION(false, 2055, "상점 소개를 입력하세요."),
    PATCH_USERS_LONG_DESCRIPTION(false, 2056, "상점 소개를 1000자 이내로 입력하세요."),
    PATCH_USERS_EMPTY_POLICY(false, 2057, "교환/반품/환불 정책을 입력하세요."),
    PATCH_USERS_LONG_POLICY(false, 2058, "교환/반품/환불 정책을 1000자 이내로 입력하세요."),
    PATCH_USERS_EMPTY_PRECAUTIONS(false, 2059, "구매전 유의사항을 입력하세요."),
    PATCH_USERS_LONG_PRECAUTIONS(false, 2060, "구매전 유의사항은 1000자 이내로 입력하세요."),
    PATCH_USERS_EMPTY_GENDER(false, 2061, "성별을 입력해주세요."),
    PATCH_USERS_CORRECT_GENDER(false, 2062, "성별에 '남' 또는 '여'라고 입력하세요."),
    PATCH_USERS_EMPTY_PHONENUM(false, 2063, "휴대폰 번호를 입력하세요."),
    PATCH_USERS_EMPTY_BIRTH(false, 2064, "생년월일을 입력하세요."),
    INCORRECT_SHAPEOF_BIRTH(false, 2065, "생년월일 형식을 맞춰주세요. ex)1990.05.21"),
    EMPTY_INQUIRING_TEXT(false,2066, "status 에 inactive 값을 넣어주세요"),
    INCORECT_INQUIRING_TEXT(false, 2067, "status에 잘못된 값이 들어왔습니다. 다시 확인해주세요"),
    INQUIRING_ALREADY_DELETED(false, 2068, "이미 삭제된 문의 입니다."),
    INQUIRING_DOESNT_EXISTS(false, 2069, "작성된 적 없는 문의 입니다."),

    //[POST] /products
    POST_REPORT_EMPTY_PRODUCT_IDX(false,2071,"상품 idx값을 입력해주세요"),
    POST_REPORT_EMPTY_REPORT_TYPE(false,2072,"신고 유형 코드를 입력해주세요"),
    INVALIDT_REPORT_TYPE(false,2073,"유효한 신고 유형 코드가 아닙니다"),

    POST_PRODUCT_EMPTY_PRODUCT_NAME(false,2075,"상품 이름을 입력해주세요"),
    POST_PRODUCT_EMPTY_CATEGORY(false,2076,"상품 카테고리 코드를 입력해주세요"),
    POST_PRODUCT_PRODUCT_EMPTY_PRICE(false,2077,"상품 가격을 입력해주세요"),
    POST_PRODUCT_EMPTY_DESCRIPTION(false,2078,"상품 설명을 입력해주세요"),
    POST_PRODUCT_PRODUCT_EMPTY_IMAGES(false,2079,"상품 이미지를 최소 1개 넣어주세요"),
    INCORRECT_TYPEOF_PRODUCT_INT(false,2080,"상품인덱스, 가격, 카테고리코드는 숫자만 입력해주세요"),
    INCORRECT_SHAPEOF_LATITUDE(false,2081,"올바른 위도 형식이 아닙니다"),
    INCORRECT_SHAPEOF_LONGITUDE(false,2082,"올바른 경도 형식이 아닙니다"),
    INVALIDT_CATEGORY_CODE_TYPE(false,2083,"유효한 카테고리 코드가 아닙니다"),
    INVALIDT_BRAND(false,2084,"유효한 브랜드가 아닙니다"),
    INVALIDT_ORDER(false,2085, "querystring order는 low,high,recent만 가능합니다"),
    INVALIDT_SOLDOUT(false,2086, "querystring soldout은 yes,no 가능합니다"),
    INVALID_DELIVERYFEE(false,2087, "querystring delieveryfee는 included, not-included, all만 가능합니다"),
    INVALID_STATUS(false,2088, "querystring status는 old,new,all만 가능합니다"),

    //[POST] likes/collections
    POST_COLLECTION_EMPTY_COLLECTION_NAME(false,2090,"찜 컬렉션명을 입력해주세요"),
    POST_COLLECTION_LONG_COLLECTION_NAME(false, 2091, "찜 컬렉션명을 10자 이내로 입력해주세요."),
    POST_COLLECTION_PRODUCT_EMPTY_PRODUCTLIST(false, 2092, "찜 컬렉션에 넣을 상품 idx 리스트를 입력해주세요"),

   //[PATCH] users
    PATCH_FAIL_REPORT_USER_PRODUCT(false, 2095, "신고당한 상품입니다. 상태 변경이 불가능합니다."),
    PATCH_FAIL_DELETE_USER_PRODUCT(false,2096, "이미 삭제한 상품입니다. 상태 변경이 불가능합니다."),
    //[GET] likes/collections
    GET_COLLECTION_PRODUCTS_INVALID_STATUS(false,2097, "querystring status는 sale 또는 not-sale만 가능합니다"),
   //[GET] likes
    GET_LIKES_INVALID_ORDER(false,2098, "querystring order는 new,past,hot,low,high만 가능합니다"),
   //[GET] brands
    GET_BRANDS_INVALID_ORDER(false,2099, "querystring order는 korean, english만 가능합니다"),
    GET_BRANDS_INVALID_FOLLOW(false,2100, "querystring follow는 true,false만 가능합니다"),
    GET_SEARCHWORD_INVALID_TYPE(false,2101, "querystring tyoe는 recent,hot만 가능합니다"),
    GET_MYPAGE_PRODUCT_INVALID_ORDER(false,2102, "querystring status는 sale,reserve,sold-out만 가능합니다"),

    //[Delete] users
    DELETE_USER_EMPTY_REASON_CATEGORY(false,2103, "탈퇴 이유 카테고리를 입력해주세요"),
    DELETE_USER_INVALID_REASON_CATEGORY(false,2104, "탈퇴 이유 카테고리는 1~6사이로 입력해주세요"),
    DELETE_USER_NOT_REMOVABLE_USER(false,2105, "판매중인 상품이 있는 경우 탈퇴가 불가능합니다. 삭제 또는 상태변경후 진행해주세요"),
    DELETE_USER_ALREADY_DELETED_USER(false,2106, "이미 삭제된 유저입니다"),
    DELETE_USER_EMPTY_REASON_TEST(false,2107, "기타의 경우 삭제이유를 10자이상 작성해주세요"),

    //[Post] payment
    POST_DEAL_CATEGORY_IS_EMPTY(false, 2120, "deal Cateogory 값이 0 또는 1 값이 아닙니다."),
    POST_EMPTY_PRODUCT_NAME(false,2121, "상품이름 값이 입력되지 않았습니다."),
    POST_EMPTY_PRICE(false, 2122, "상품 가격 값이 입력되지 않았습니다."),
    POST_EMPTY_PAY_METHOD(false, 2123, "결제수단이 입력되지 않았습니다"),
    POST_WRONG_PAY_METHOD(false, 2124, "이용할 수 없는 결제 수단 입니다"),
    POST_DISAGREE_PAYMENT(false, 2125, "결제 약관에 동의 하지 않았습니다. 동의 후 이용헤주세요."),

    //[Post] Review
    EMPTY_REVIEW_TEXT(false, 2130, "리뷰내용 부분을 작성하지 않으셨습니다."),
    INVALID_REVIEW_TEXT_LENGTH(false, 2131, "리뷰 내용은 빈칸이거나 1000자를 넘으면 안됩니다."),
    INVALID_REVIEW_SCORE(false, 2132, "리뷰 별점은 0 ~ 5점 사이로 줘야 합니다."),
    ALREADY_WRITING_REVIEW(false, 2133, "이미 해당 상품에 대한 리뷰를 작성했습니다."),
    NOT_PROPER_CATEGORY(false, 2134, "신고 유형을 잘못 입력하였습니다."),
    EMPTY_COMMENT_TEXT(false, 2135, "댓글을 달려면 내용을 작성해야 합니다."),
    INVALID_COMMENT_LENGTH(false,2136, "댓글은 100자를 넘을 수 없습니다."),
    ALREADY_EXIST_COMMENT(false, 2137, "이미 해당 리뷰에 댓글을 남겼습니다"),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    POST_USERS_EXISTS_STORENAME(false, 3015, "이미 존재하는 상점 이름입니다"),
    FAILED_TO_INQUIRING (false, 3010 , "문의할 수 없는 상태입니다."),
    FAILED_TO_INQUIRED (false, 3011, "해당 상점은 현재 문의가 불가능 합니다."),
    FAILED_TO_WRITE_INQUIRY (false, 3012, "문의 작성에 실패하였습니다."),
    FAILED_TO_LOAD_INQUIRY(false,3013, "상품 문의를 조회할 수 없는 상태입니다"),

    // sms
    FAILED_TO_COOLSMS(false,3015,"CoolSMS Error."),

    FAILED_TO_CHECK_AUTH(false,3016,"인증 번호가 일치하지 않습니다."),

    NOT_AVALIABLE_USER_STORE(false,3020, "조회할 수 없는 유저의 상점입니다."),

    //[POST] products/report
    POST_REPORT_EXIST_REPORT(false, 3021, "이미 신고한 상품입니다"),

    //[POST] favorites
    NOT_AVALIABLE_ADD_FOLLOW(false, 3040, "해당 상점은 팔로잉 할 수 없는 상태입니다."),
    FOLLOW_BRAND_FAIL(false, 3045, "브랜드 팔로우에 실패하였습니다."),


    //[DELETE] favorites
    FOLLOW_CANCEL_FAIL(false, 3050, "팔로위 취소 하지 못했습니다."),
    FOLLOW_DOESNT_EXISTS(false,3047, "해당 브랜드를 팔로우한적이 없습니다."),


    //[POST] likes
    FAILED_TO_PRODUCT_LIKE(false,3051,"찜 생성에 실패하였습니다"),
    //[PATCH] likes
    FAILED_TO_CANCEL_LIKE(false,3052,"찜 취소에 실패하였습니다"),
    //[POST] likes/collections
    FAILED_TO_CREATE_COLLECTION(false,3053,"찜 컬렉션 생성에 실패하였습니다"),
    //[PATCH] likes/collections
    FAILED_TO_UPDATE_COLLECTION(false,3054,"찜 컬렉션 수정에 실패하였습니다"),
    FAILED_TO_CREATE_COLLECTION_PRODUCT(false,3055,"상품을 찜 컬렉션으로 이동하는 것에 실패하였습니다"),

    //[GET]
    NOT_AVALIABLE_GET_USER_STATE(false, 3060, "유저정보를 수정할 수 없는 상태입니다."),

    //[POST] favorites
    DUPLICATED_FOLLOW_BRAND(false, 3065, "이미 팔로우를 진행했습니다."),
    NOT_EXISTS_BRAND(false, 3066, "존재하지 않는 브랜드 입니다."),
    NOT_AVALIABLE_BRAND_STATUS(false, 3067, "삭제된 브랜드 입니다."),

    //[DELETE] likes
    FAILED_TO_DELETE_COLLECTION(false, 3068, "찜 컬렉션 삭제에 실패했습니다"),

    //[DELETE] searches
    FAILED_TO_DELETE_ALL_SEARCHES(false, 3069, "삭제할 검색어가 없습니다"),
    INCORRENT_USER_OF_SEARCHIDX(false, 3070, "본인의 검색어만 삭제할 수 있습니다"),
    NOT_EXISTS_SEARCH_IDX(false, 3071, "삭제되었거나 존재하지 않는 searchIdx 입니다."),

    //[Post] payment
    ALREADY_EXIST_ORDER(false, 3100, "이미 존재하는 결제 내역입니다"),
    NOT_AVALIABLE_SELLER_STATUS(false, 3101, "구매할 수 없는 상점입니다"),
    PRODUCT_HAS_REPORTS(false, 3102, "해당 상품은 신고 받은 이력이 있습니다"),
    NOT_AVALIABLE_BUYER_STATUS(false, 3103, "현재 회원의 상태는 구매가 불가능합니다 (탈퇴 또는 정지)"),
    ALREADY_SOLD_OUT_PRODUCT(false, 3104, "이미 판매된 상품입니다."),
    USED_BUNGAE_POINTS_WRONG(false, 3105, "회원이 가지고 있는 번개 포인트보다 더 많은 포인트를 사용하였습니다."),

    //[Post] Review
    EXPIRED_REVIEW_WRITE(false, 3110, "리뷰 작성 기한이 지났습니다."),
    NOT_AVALIABLE_WRITE_SELLER_STATUS(false, 3111, "해당 상점이 리뷰를 작성할 수 있는 상태가 아닙니다."),
    NOT_REPORT_USER(false, 3112, "해당 리뷰의 주인이 아니기 떄문에 리뷰를 신고할 수 없습니다."),
    ALREADY_REPORT_REVIEW(false, 3113, "이미 신고된 리뷰 입니다."),

    //[Patch] Review
    NOT_EXIST_REVIEW(false, 3115, "수정할 수 있는 리뷰가 없습니다."),
    NOT_EXIST_REVIEW_DELETE(false, 3116,"삭제할 리뷰가 없습니다."),

    //[Delete] Users
    DELETED_USER(false,3120, "회원탈퇴한 유저입니다"),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_STORENAME(false,4014,"상점이름 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
