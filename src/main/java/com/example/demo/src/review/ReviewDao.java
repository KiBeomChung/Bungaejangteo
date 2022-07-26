package com.example.demo.src.review;

import com.example.demo.src.review.model.DeleteReviewReq;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchModifyReviewReq;
import com.example.demo.src.review.model.PostRegisterReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ReviewDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetReviewRes> getReviewList(int id){

        String getReviewListQuery = "select Users.imageUrl, Buy.id, Users.storeName, reviewScore, PayResult.bungaePay, reviewText, Products.name,\n" +
                "CASE WHEN timestampdiff(second, Review.createdAt, current_timestamp) < 60\n" +
                "                THEN concat(timestampdiff(second,Review.createdAt, current_timestamp), ' 초 전')\n" +
                "            WHEN timestampdiff(minute, Review.createdAt, current_timestamp) < 60\n" +
                "                THEN concat(timestampdiff(minute, Review.createdAt, current_timestamp), ' 분 전')\n" +
                "            WHEN timestampdiff(hour,Review.createdAt, current_timestamp) < 24\n" +
                "                THEN concat(timestampdiff(hour,Review.createdAt, current_timestamp), ' 시간 전')\n" +
                "            WHEN timestampdiff(day,Review.createdAt, current_timestamp) < 7\n" +
                "                THEN concat(timestampdiff(day,Review.createdAt, current_timestamp), ' 일 전')\n" +
                "            WHEN timestampdiff(week,Review.createdAt, current_timestamp) < 4\n" +
                "                THEN concat(timestampdiff(week,Review.createdAt, current_timestamp), ' 주 전')\n" +
                "            WHEN timestampdiff(month,Review.createdAt, current_timestamp) < 12\n" +
                "                THEN concat(timestampdiff(month,Review.createdAt, current_timestamp), ' 달 전')\n" +
                "            WHEN timestampdiff(year,Review.createdAt, current_timestamp) < 1000\n" +
                "                THEN concat(timestampdiff(year,Review.createdAt, current_timestamp), ' 년 전')\n" +
                "           END AS 'createdAt'\n" +
                "from Review\n" +
                "inner join Buy on Review.reviewId = Buy.reviewId\n" +
                "inner join Users on Buy.id = Users.id\n" +
                "inner join Products on Products.productId = Review.productId\n" +
                "inner join PayResult on Buy.buyId = PayResult.buyId\n" +
                "inner join Sell on Sell.reviewId = Review.reviewId\n" +
                "and Sell.id = ?\n" +
                "where Review.status = 'active'";
        int getReviewListParam = id;
        return this.jdbcTemplate.query(getReviewListQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("imageUrl"),
                        rs.getInt("id"),
                        rs.getString("storeName"),
                        rs.getInt("reviewScore"),
                        rs.getString("bungaePay"),
                        rs.getString("reviewText"),
                        rs.getString("name"),
                        rs.getString("createdAt")
                ), getReviewListParam);
    }

    public int registerReview(PostRegisterReviewReq postRegisterReviewReq) {

        String registerReviewQuery = "insert into Review(reviewText, reviewScore, productId) values(?,?,?)";
        Object[] registerReviewParams = new Object[] {postRegisterReviewReq.getReviewText(), postRegisterReviewReq.getReviewScore(), postRegisterReviewReq.getProductId()};

        this.jdbcTemplate.update(registerReviewQuery, registerReviewParams);

        String lastInsertIdStr = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdStr, int.class);
    }

    public int registerReviewImg(int lastInsertId, List<String> imageUrl) {

        int registerImgNum = 0;

        for(String url : imageUrl) {
            String registerReviewImgQuery = "insert into ReviewImage(reviewId, reviewImageUrl) values(?,?)";
            Object[] registerReviewImgParams = new Object[]{lastInsertId, url};
            this.jdbcTemplate.update(registerReviewImgQuery, registerReviewImgParams);
            registerImgNum++;
        }
        return registerImgNum;
    }

    public int checkCreatedAt(int userId) {
        String checkCreatedAtQuery = "select timestampdiff(day, OrderInfo.createdAt, current_timestamp) from OrderInfo where OrderInfo.id = ? ";
        return this.jdbcTemplate.queryForObject(checkCreatedAtQuery, int.class, userId);
    }

    public int checkSellerStatus(int productId) {
        String checkSellerStatusQuery = "select exists(select * from Products inner join Users on Users.id = Products.userId where Products.productId = ? and Users.status = 'DELETED')";
        return this.jdbcTemplate.queryForObject(checkSellerStatusQuery, int.class, productId);  //확인
    }

    public int checkBuyerStatus(int userId) {
        String checkBuyerStatusQuery = "select exists(select * from Users where Users.id = ? and Users.status = 'DELETED')"; //확인
        return this.jdbcTemplate.queryForObject(checkBuyerStatusQuery, int.class, userId);
    }

    public int isAlreadyWriting(int productId) {
        String isAlreadyWritingQuery = "select exists(select * from Review where Review.productId = ?)";

        return this.jdbcTemplate.queryForObject(isAlreadyWritingQuery, int.class, productId);
    }

    public int updateBuySell(int userId, int lastInsertId, PostRegisterReviewReq postRegisterReviewReq) {
        String updateBuySellQuery = "update Buy as b, Sell as s\n" +
                "set b.reviewId = ?,\n" +
                "    s.reviewId = ?\n" +
                "where b.connectId = s.sellId and b.id = ? and s.id = (select Products.userId from Products where Products.productId = ?)\n" +
                "and s.orderId = (select OrderInfo.orderId from OrderInfo where OrderInfo.id = ? and OrderInfo.productId = ?)";
        Object[] updateBuySellParams = new Object[]{lastInsertId, lastInsertId, userId, postRegisterReviewReq.getProductId(), userId, postRegisterReviewReq.getProductId()};
        return this.jdbcTemplate.update(updateBuySellQuery, updateBuySellParams);
    }

    public int modifyReview(PatchModifyReviewReq patchModifyReviewReq) {
        String modifyReviewQuery = "update Review\n" +
                "set reviewText = ?,\n" +
                "    reviewScore = ?\n" +
                "where Review.reviewId = (select tmp.reviewId from (select Review.reviewId from Review where Review.reviewId = ?) as tmp)";
        Object[] modifyReviewParams = new Object[] {patchModifyReviewReq.getReviewText(), patchModifyReviewReq.getReviewScore(),
                patchModifyReviewReq.getReviewId()};

        return this.jdbcTemplate.update(modifyReviewQuery, modifyReviewParams);
    }

    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where id = ? and status = 'DELETED')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public int isExistReview(PatchModifyReviewReq patchModifyReviewReq) {
        String isExistReviewQuery = "select exists(select * from Review where Review.reviewId = ?)";

        return this.jdbcTemplate.queryForObject(isExistReviewQuery, int.class, patchModifyReviewReq.getReviewId());
    }

    public int deleteReviewImage(DeleteReviewReq deleteReviewReq) {
        String deleteReviewImageQuery = "delete from ReviewImage where ReviewImage.reviewId = ?";

        return this.jdbcTemplate.update(deleteReviewImageQuery, deleteReviewReq.getReviewId());
    }

    public int updateBuySell(DeleteReviewReq deleteReviewReq) {
        String updateBuySellQuery = "update Buy as b, Sell as s\n" +
                "set b.reviewId = null,\n" +
                "    s.reviewId = null\n" +
                "where b.connectId = s.sellId and b.reviewId = ? and s.reviewId = ?";
        Object[] updateBuySellParams = new Object[]{deleteReviewReq.getReviewId(), deleteReviewReq.getReviewId()};

        return this.jdbcTemplate.update(updateBuySellQuery, updateBuySellParams);
    }

    public int deleteReview(DeleteReviewReq deleteReviewReq) {
        String deleteReviewQuery = "delete from Review where Review.reviewId = ?";

        return this.jdbcTemplate.update(deleteReviewQuery, deleteReviewReq.getReviewId());
    }
}
