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

        String getReviewListQuery = "select Users.imageUrl, Buy.id, Users.storeName, reviewScore, reviewText, Products.name, Review.createdAt\n" +
                "from Review\n" +
                "inner join Buy on Review.reviewId = Buy.reviewId\n" +
                "inner join Users on Buy.id = Users.id\n" +
                "inner join Products on Products.productId = Review.productId\n" +
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
                        rs.getString("reviewText"),
                        rs.getString("name"),
                        rs.getTimestamp("createdAt")
                ), getReviewListParam);
    }
}
