package com.example.demo.src.product;

//import com.example.demo.src.product.model*
import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.sql.DataSource;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductRes> getRecommendProducts(int userIdx) {
        String GetProductQuery = "SELECT Products.productId,name,price,region,isSafePayment," +
                "case" +
                "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전')" +
                "    when (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전')" +
                "    when (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전')" +
                "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전')" +
                " END AS elapsedTime, imageUrl,likeCount" +
                " FROM (Products left outer join (SELECT imageUrl, productId" +
                " FROM (SELECT *" +
                "       FROM ProductImages" +
                "       ORDER BY createdAt)b" +
                " GROUP BY productId" +
                " )as imageTable" +
                " on imageTable.productId = Products.productId)" +
                " left outer join (select productId,count(*) as likeCount from Likes group by productId)as c" +
                " on Products.productId =  c.productId " +
                " where Products.status ='SALE' " +
                " order by rand() " ;

        return this.jdbcTemplate.query(GetProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("Products.productId"),
                        rs.getString("imageUrl"),
                        this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ?) as b",
                              (rs2, rowNum2) -> new Integer(
                                      rs2.getInt("b")),
                                userIdx,rs.getInt("Products.productId")),
                        rs.getInt("price"),
                        rs.getString("name"),
                        rs.getString("region"),
                        rs.getString("elapsedTime"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("likeCount")
                       ));
    }

    public int  createReport(int userIdx, Integer productIdx,PostReportReq postReportReq) throws BaseException {
        String createReportQuery = "insert into ProductReports (userId, productId, reportType, detailReason) VALUES (?,?,?,?)";
        Object[] createReportParams = new Object[]{userIdx,productIdx, postReportReq.getReportType(),postReportReq.getDetailReason()};
        return this.jdbcTemplate.update(createReportQuery, createReportParams);
    }

    public int getReport(int userIdx,int productIdx) throws BaseException{
        String getReportQuery = "select exists(select * from ProductReports where userId = ? and productId = ?)";
        Object[] getReportParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.queryForObject(getReportQuery,
                int.class,
                getReportParams);

    }

    public int createProduct(int userIdx, PostProductReq postProductReq) throws BaseException {
        String createProductQuery = "insert into Products(userId,name,category,price,isDeliveryIncluded,count,isOld,isExchangeAvailable,isSafePayment,region,latitude,longitude,description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{userIdx,postProductReq.getName(),postProductReq.getCategory(),postProductReq.getPrice(),postProductReq.getIsDeliveryIncluded(),postProductReq.getCount(),
                postProductReq.getIsOld(),postProductReq.getIsExchangeAvailable(),postProductReq.getIsSafePayment(),postProductReq.getRegion(),postProductReq.getLatitude(),postProductReq.getLongitude(),postProductReq.getDescription()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }


    @Transactional
   public int createProductImages(int productIdx, List<String> Images) throws BaseException {
        int result = 0;
        for (int i = 0 ; i < Images.size(); i++){
            String createProductImagesQuery = "insert into ProductImages (productId,imageUrl) VALUES (?,?)";
            Object[] createProductImagesParams = new Object[]{productIdx,(Images.get(i))};
            result += this.jdbcTemplate.update(createProductImagesQuery, createProductImagesParams);
        }
        return result;
    }

    @Transactional
    public int createProductTags(int productIdx, List<String> tags) throws BaseException {
        int result = 0;
        for (int i = 0 ; i < tags.size(); i++){
            String createProductTagsQuery = "insert into ProductTags (productId,tag) VALUES (?,?)";
            Object[] createProductTagsParams = new Object[]{productIdx,(tags.get(i))};
            result += this.jdbcTemplate.update(createProductTagsQuery, createProductTagsParams);
        }
        return result;
    }

    public GetDetailProductRes getDetailProduct(int userIdx,int productIdx) {
        String GetProductQuery = "SELECT Products.productId,name,price,region,isSafePayment,isDeliveryIncluded,isOld,isExchangeAvailable,count,\n" +
                "case \n" +
                "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전') \n" +
                "\twhen (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전') \n" +
                "\twhen (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전') \n" +
                "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전') \n" +
                "END AS elapsedTime,\n" +
                "case \n" +
                "    when (likeCount is null) then 0\n" +
                "    when (likeCount is not null) then likeCount\n" +
                "end as likeCount, exists(select * from Likes where userId = ? AND productId = Products.productId) as isExist,description,Products.status\n" +
                "from Products\n" +
                "left outer join (select productId,count(*) as likeCount from Likes group by productId)as c\n" +
                "on Products.productId =  c.productId where Products.productId = ?" ;

        return this.jdbcTemplate.queryForObject(GetProductQuery,
                (rs, rowNum) -> new GetDetailProductRes(
                        rs.getInt("Products.productId"),
                        rs.getInt("price"),
                        rs.getString("name"),
                        rs.getInt("isExist"),
                        rs.getString("region"),
                        rs.getString("elapsedTime"),
                        rs.getInt("isDeliveryIncluded"),
                        rs.getInt("count"),
                        rs.getInt("isOld"),
                        rs.getInt("isExchangeAvailable"),
                        rs.getString("description"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("likeCount"),
                        this.jdbcTemplate.query("select imageUrl from ProductImages where productId = ?",
                                (rs2,rosNum2) -> new String(
                                        rs2.getString("imageUrl")
                                ), rs.getInt("Products.productId")),
                        this.jdbcTemplate.query("select tag from ProductTags where productId = ?",
                                (rs3,rosNum3) -> new String(
                                        rs3.getString("tag")
                                ), rs.getInt("Products.productId")),
                        rs.getString("status"),
                        //상점
                        this.jdbcTemplate.queryForObject("select Users.id,storeName,imageUrl,AverageRatings,ProductNum,followerNum,exists(select * from Following inner join Follow on Follow.followingId = Following.id where Following.followingId = 1 and followerId = 2)as followed,ProductNum\n" +
                                        "from Users inner join (select round(avg(Review.reviewScore), 2) as averageRatings from Review where productId in (select productId from Products where userId = ?))as b\n" +
                                        "inner join (select count(*) as ProductNum from Products where userId = ?)as c \n" +
                                        "inner join (select count(*) as followerNum from Following inner join Follow on Follow.followingId = Following.id where Following.followingId = ?)as d\n" +
                                        "where id in (select userId from Products where Products.productId = ?)",
                                (rs4,rosNum4) -> new storeInfo(
                                        rs4.getInt("Users.id"),
                                        rs4.getString("imageUrl"),
                                        rs4.getString("storeName"),
                                        rs4.getDouble("AverageRatings"),
                                        rs4.getInt("followerNum"),
                                        rs4.getBoolean("followed"),
                                        rs4.getInt("ProductNum"),
                                        this.jdbcTemplate.query("SELECT Products.productId,name,price,region,isSafePayment,\n" +
                                                        "case \n" +
                                                        "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전') \n" +
                                                        "\twhen (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전') \n" +
                                                        "\twhen (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전') \n" +
                                                        "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전') \n" +
                                                        "END AS elapsedTime,\n" +
                                                        "case \n" +
                                                        "    when (likeCount is null) then 0\n" +
                                                        "    when (likeCount is not null) then likeCount\n" +
                                                        "end as likeCount, exists(select * from Likes where userId = ? AND productId = Products.productId) as isExist,imageUrl\n" +
                                                        "from (Products left outer join (SELECT imageUrl, productId\n" +
                                                        "FROM (SELECT *\n" +
                                                        "      FROM ProductImages\n" +
                                                        "      ORDER BY createdAt)b\n" +
                                                        "GROUP BY productId )as imageTable \n" +
                                                        "on imageTable.productId = Products.productId) \n" +
                                                        "left outer join (select productId,count(*) as likeCount from Likes group by productId)as c\n" +
                                                        "on Products.productId =  c.productId\n" +
                                                        "where Products.userId = ? and Products.status = 'SALE' limit 6",
                                                (rs5,rosNum5) -> new GetProductRes(
                                                        rs5.getInt("Products.productId"),
                                                        rs5.getString("imageUrl"),
                                                        rs5.getInt("isExist"),
                                                        rs5.getInt("price"),
                                                        rs5.getString("name"),
                                                        rs5.getString("region"),
                                                        rs5.getString("elapsedTime"),
                                                        rs5.getInt("isSafePayment"),
                                                        rs5.getInt("likeCount")
                                                ), userIdx,userIdx)
                                ), userIdx,userIdx,userIdx,rs.getInt("Products.productId")),
                        this.jdbcTemplate.queryForObject("select count(*) as reviewNum\n" +
                                        "from Review\n" +
                                        "inner join Buy on Review.reviewId = Buy.reviewId\n" +
                                        "inner join Users on Buy.id = Users.id\n" +
                                        "inner join Sell on Sell.reviewId = Review.reviewId\n" +
                                        "and Sell.id = ?\n" +
                                        "where Review.status = 'active'",
                                (rs6, rowNum6) -> new Integer(
                                        rs6.getInt("reviewNum")),
                                userIdx),
                        this.jdbcTemplate.query("select Review.reviewId,Users.storeName, reviewScore, reviewText,\n" +
                                        "                CASE WHEN timestampdiff(second, Review.createdAt, current_timestamp) < 60\n" +
                                        "                THEN concat(timestampdiff(second,Review.createdAt, current_timestamp), ' 초 전')\n" +
                                        "                WHEN timestampdiff(minute, Review.createdAt, current_timestamp) < 60\n" +
                                        "                THEN concat(timestampdiff(minute, Review.createdAt, current_timestamp), ' 분 전')\n" +
                                        "                WHEN timestampdiff(hour,Review.createdAt, current_timestamp) < 24\n" +
                                        "                THEN concat(timestampdiff(hour,Review.createdAt, current_timestamp), ' 시간 전')\n" +
                                        "                WHEN timestampdiff(day,Review.createdAt, current_timestamp) < 7\n" +
                                        "                THEN concat(timestampdiff(day,Review.createdAt, current_timestamp), ' 일 전')\n" +
                                        "                WHEN timestampdiff(week,Review.createdAt, current_timestamp) < 4\n" +
                                        "                THEN concat(timestampdiff(week,Review.createdAt, current_timestamp), ' 주 전')\n" +
                                        "                WHEN timestampdiff(month,Review.createdAt, current_timestamp) < 12\n" +
                                        "                THEN concat(timestampdiff(month,Review.createdAt, current_timestamp), ' 달 전')\n" +
                                        "                WHEN timestampdiff(year,Review.createdAt, current_timestamp) < 1000\n" +
                                        "                THEN concat(timestampdiff(year,Review.createdAt, current_timestamp), ' 년 전')\n" +
                                        "                END AS createdAt\n" +
                                        "                from Review\n" +
                                        "                inner join Buy on Review.reviewId = Buy.reviewId\n" +
                                        "                inner join Users on Buy.id = Users.id\n" +
                                        "                inner join Products on Products.productId = Review.productId\n" +
                                        "                inner join Sell on Sell.reviewId = Review.reviewId\n" +
                                        "                and Sell.id = ?\n" +
                                        "                where Review.status = 'active'\n" +
                                        "                limit 2",
                                (rs7, rowNum7) -> new ReviewInfo(
                                        rs7.getString("Users.storeName"),
                                        rs7.getDouble("reviewScore"),
                                        rs7.getString("reviewText"),
                                        rs7.getString("createdAt")
                                ), userIdx),
                        this.jdbcTemplate.queryForObject("select count(*) from Inquiring where inquiringId = ?",
                                (rs8, rowNum8) -> new Integer(
                                        rs8.getInt("count(*)")),
                                userIdx)

                ),userIdx,productIdx);


    }

    public int isDeletedUser(int userIdx) {
        String isDeletedUserQuery = "select exists(select * from Users where id = ? and status ='DELETED')";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, userIdx);
    }

    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where id = ? and status = 'DELETED')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public List<GetRelatedProdcutRes> getRelatedProduct(int userId, int productId) {
        String getRelatedProductQuery = "select Products.productId, ProductImages.imageUrl, Products.price, Products.name, Products.isSafePayment, ProductTags.tag\n" +
                "from ProductTags inner join Products on Products.productId = ProductTags.productId\n" +
                "inner join (SELECT imageUrl, productId FROM (SELECT * FROM ProductImages ORDER BY createdAt)a GROUP BY productId) ProductImages on ProductImages.productId = Products.productId\n" +
                "where ProductTags.tag in (select ProductTags.tag from ProductTags inner join Products on Products.productId = ProductTags.productId and ProductTags.productId = ?) order by rand()\n";

        return this.jdbcTemplate.query(getRelatedProductQuery,
                (rs, rowNum) -> new GetRelatedProdcutRes(
                        rs.getInt("productId"),
                        rs.getString("imageUrl"),
                        rs.getInt("price"),
                        rs.getString("name"),
                        this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ? and status = 'NORMAL') as isLike",
                                (rs3, rowNum3) -> new Integer(
                                        rs3.getInt("isLike")),
                                userId, rs.getInt("productId")),
                        rs.getInt("isSafePayment"),
                        rs.getString("tag")
                ), productId);
    }

}


