package com.example.demo.src.review;

import com.example.demo.src.review.model.GetReviewRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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

//    public List<GetDateRes> getCreatedDate(int id){
//        String getCreatedDateQuery = "select Review.createdAt\n" +
//                "from Review\n" +
//                "inner join Buy on Review.reviewId = Buy.reviewId\n" +
//                "inner join Users on Buy.id = Users.id\n" +
//                "inner join Products on Products.productId = Review.productId\n" +
//                "inner join PayResult on Buy.buyId = PayResult.buyId\n" +
//                "inner join Sell on Sell.reviewId = Review.reviewId\n" +
//                "and Sell.id = ?\n" +
//                "where Review.status = 'active'";
//                int getCreatedDateParam = id;
//        return this.jdbcTemplate.query(getCreatedDateQuery,
//                (rs, rowNum) -> new GetDateRes(
//                        rs.getTimestamp("createdAt")
//                ), getCreatedDateParam);
//    }
}
