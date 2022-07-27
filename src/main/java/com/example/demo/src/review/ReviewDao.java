package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
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

    public List<GetReviewRes> getReviewList(int id) {

        String getCommentQuery = "select Users.storeName, ReviewComment.commentText,CASE WHEN timestampdiff(second, ReviewComment.createdAt, current_timestamp) < 60\n" +
                "                                THEN concat(timestampdiff(second,ReviewComment.createdAt, current_timestamp), ' 초 전')\n" +
                "                            WHEN timestampdiff(minute, ReviewComment.createdAt, current_timestamp) < 60\n" +
                "                                THEN concat(timestampdiff(minute, ReviewComment.createdAt, current_timestamp), ' 분 전')\n" +
                "                           WHEN timestampdiff(hour,ReviewComment.createdAt, current_timestamp) < 24\n" +
                "                               THEN concat(timestampdiff(hour,ReviewComment.createdAt, current_timestamp), ' 시간 전')\n" +
                "                            WHEN timestampdiff(day,ReviewComment.createdAt, current_timestamp) < 7\n" +
                "                                THEN concat(timestampdiff(day,ReviewComment.createdAt, current_timestamp), ' 일 전')\n" +
                "                            WHEN timestampdiff(week,ReviewComment.createdAt, current_timestamp) < 5\n" +
                "                                THEN concat(timestampdiff(week,ReviewComment.createdAt, current_timestamp), ' 주 전')\n" +
                "                            WHEN timestampdiff(month,ReviewComment.createdAt, current_timestamp) < 12\n" +
                "                                THEN concat(timestampdiff(month,ReviewComment.createdAt, current_timestamp), ' 달 전')\n" +
                "                            WHEN timestampdiff(year,ReviewComment.createdAt, current_timestamp) < 1000\n" +
                "                                THEN concat(timestampdiff(year,ReviewComment.createdAt, current_timestamp), ' 년 전')\n" +
                "                           END AS 'createdAt'\n" +
                "from Review inner join ReviewComment on Review.reviewId = ReviewComment.reviewId\n" +
                "inner join Users on Users.id = ReviewComment.id\n" +
                "where ReviewComment.id = ?  and ReviewComment.reviewId = ?";

        String getReviewListQuery = "select Review.reviewId, Users.imageUrl, Buy.id, Users.storeName, reviewScore, reviewText, Products.name,\n" +
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
                "inner join Sell on Sell.reviewId = Review.reviewId\n" +
                "and Sell.id = ?\n" +
                "where Review.status = 'active'";
        int getReviewListParam = id;
        return this.jdbcTemplate.query(getReviewListQuery,
                   (rs, rowNum) -> new GetReviewRes(
                           rs.getInt("reviewId"),
                           rs.getString("imageUrl"),
                           rs.getInt("id"),
                           rs.getString("storeName"),
                           rs.getInt("reviewScore"),
                           rs.getString("reviewText"),
                           rs.getString("name"),
                           rs.getString("createdAt")
                   ), getReviewListParam);

    }

    public int checkComment(int reviewId) {
        String checkCommentQuery = "select exists(select * from ReviewComment inner join Review \n" +
                "    on Review.reviewId = ReviewComment.reviewId where Review.reviewId = ?)";

        return this.jdbcTemplate.queryForObject(checkCommentQuery, int.class, reviewId);
    }

    public int checkImage(int reviewId) {
        String checkImageQuery = "select exists(select * from ReviewImage where ReviewImage.reviewId = ?)";
        return this.jdbcTemplate.queryForObject(checkImageQuery, int.class, reviewId);

    }

    public GetCommentRes getReviewComment(int reviewId, int id) {
        String getCommentQuery = "select Users.storeName, ReviewComment.commentText,CASE WHEN timestampdiff(second, ReviewComment.createdAt, current_timestamp) < 60\n" +
                "                                THEN concat(timestampdiff(second,ReviewComment.createdAt, current_timestamp), ' 초 전')\n" +
                "                            WHEN timestampdiff(minute, ReviewComment.createdAt, current_timestamp) < 60\n" +
                "                                THEN concat(timestampdiff(minute, ReviewComment.createdAt, current_timestamp), ' 분 전')\n" +
                "                           WHEN timestampdiff(hour,ReviewComment.createdAt, current_timestamp) < 24\n" +
                "                               THEN concat(timestampdiff(hour,ReviewComment.createdAt, current_timestamp), ' 시간 전')\n" +
                "                            WHEN timestampdiff(day,ReviewComment.createdAt, current_timestamp) < 7\n" +
                "                                THEN concat(timestampdiff(day,ReviewComment.createdAt, current_timestamp), ' 일 전')\n" +
                "                            WHEN timestampdiff(week,ReviewComment.createdAt, current_timestamp) < 5\n" +
                "                                THEN concat(timestampdiff(week,ReviewComment.createdAt, current_timestamp), ' 주 전')\n" +
                "                            WHEN timestampdiff(month,ReviewComment.createdAt, current_timestamp) < 12\n" +
                "                                THEN concat(timestampdiff(month,ReviewComment.createdAt, current_timestamp), ' 달 전')\n" +
                "                            WHEN timestampdiff(year,ReviewComment.createdAt, current_timestamp) < 1000\n" +
                "                                THEN concat(timestampdiff(year,ReviewComment.createdAt, current_timestamp), ' 년 전')\n" +
                "                           END AS 'createdAt'\n" +
                "from Review inner join ReviewComment on Review.reviewId = ReviewComment.reviewId\n" +
                "inner join Users on Users.id = ReviewComment.id\n" +
                "where ReviewComment.id = ?  and ReviewComment.reviewId = ?";

        Object[] getCommentParams = new Object[]{id, reviewId};

        return this.jdbcTemplate.queryForObject(getCommentQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getString("storeName"),
                        rs.getString("commentText"),
                        rs.getString("createdAt")
                ), getCommentParams);
    }

    public List<GetReviewImageRes> getReviewImages(int reviewId) {
        String getReviewImagesQuery = "select Review.reviewId, ReviewImage.reviewImageUrl\n" +
                "from Review inner join ReviewImage on Review.reviewId = ReviewImage.reviewId\n" +
                "where ReviewImage.reviewId = ?";
        return this.jdbcTemplate.query(getReviewImagesQuery,
                (rs, rowNum) -> new GetReviewImageRes(
                        rs.getInt("reviewId"),
                        rs.getString("reviewImageUrl")
                ), reviewId);
    }

    public int registerReview(PostRegisterReviewReq postRegisterReviewReq) {

        String registerReviewQuery = "insert into Review(reviewText, reviewScore, productId) values(?,?,?)";
        Object[] registerReviewParams = new Object[]{postRegisterReviewReq.getReviewText(), postRegisterReviewReq.getReviewScore(), postRegisterReviewReq.getProductId()};

        this.jdbcTemplate.update(registerReviewQuery, registerReviewParams);

        String lastInsertIdStr = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdStr, int.class);
    }

    public int registerReviewImg(int lastInsertId, List<String> imageUrl) {

        int registerImgNum = 0;

        for (String url : imageUrl) {
            String registerReviewImgQuery = "insert into ReviewImage(reviewId, reviewImageUrl) values(?,?)";
            Object[] registerReviewImgParams = new Object[]{lastInsertId, url};
            this.jdbcTemplate.update(registerReviewImgQuery, registerReviewImgParams);
            registerImgNum++;
        }
        return registerImgNum;
    }

    public int checkCreatedAt(int productId) {
        String checkCreatedAtQuery = "select timestampdiff(day, OrderInfo.createdAt, current_timestamp) from OrderInfo where OrderInfo.productId = ? ";
        return this.jdbcTemplate.queryForObject(checkCreatedAtQuery, int.class, productId);
    }

    public int checkCreatedAt2(int reviewId) {
        String checkCreatedAtQuery = "select timestampdiff(day, OrderInfo.createdAt, current_timestamp) from OrderInfo inner join Review on Review.reviewId = OrderInfo.reviewId and Review.reviewId = ?";
        return this.jdbcTemplate.queryForObject(checkCreatedAtQuery, int.class, reviewId);
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
        Object[] modifyReviewParams = new Object[]{patchModifyReviewReq.getReviewText(), patchModifyReviewReq.getReviewScore(),
                patchModifyReviewReq.getReviewId()};

        return this.jdbcTemplate.update(modifyReviewQuery, modifyReviewParams);
    }

    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where id = ? and status = 'DELETED')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public int isExistReview(int reviewId) {
        String isExistReviewQuery = "select exists(select * from Review where Review.reviewId = ?)";

        return this.jdbcTemplate.queryForObject(isExistReviewQuery, int.class, reviewId);
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

    public int reportReview(PostReviewReportReq postReviewReportReq) {
        String reportReviewQuery = "insert into ReviewReports(reportsCategory, reviewId) values(?,?)";
        Object[] reportReviewParams = new Object[]{postReviewReportReq.getReportCategory(), postReviewReportReq.getReviewId()};

        return this.jdbcTemplate.update(reportReviewQuery, reportReviewParams);
    }

    public int checkReportAvailable(int userId, int reviewId) {
        String checkReportAvailableQuery = "select exists(select * from Review inner join Products on Products.productId = Review.productId and Review.reviewId = ? where Products.userId = ?)";
        Object[] checkReportAvailableParams = new Object[]{userId, reviewId};

        return this.jdbcTemplate.queryForObject(checkReportAvailableQuery, int.class, checkReportAvailableParams);
    }

    public int isAlreadyReport(int reviewId) {
        String isAlreadyReportQuery = "select exists(select * from ReviewReports where reviewId = ?)";

        return this.jdbcTemplate.queryForObject(isAlreadyReportQuery, int.class, reviewId);
    }

    public int deleteReviewReport(int reviewId) {
        String deleteReviewReportQuery = "delete from ReviewReports where ReviewReports.reviewId = ?";

        return this.jdbcTemplate.update(deleteReviewReportQuery, reviewId);

    }

    public int registerComment(int id, PostRegisterCommentReq postRegisterCommentReq) {
        String registerCommentQuery = "insert into ReviewComment(commentText, reviewId, id) values(?, ?, ?)";
        Object[] registerCommentParams = new Object[] {postRegisterCommentReq.getCommentText(), postRegisterCommentReq.getReviewId(), id};

        return this.jdbcTemplate.update(registerCommentQuery, registerCommentParams);
    }

    public int isAlreadyComment(int reviewId, int userId) {
        String isAlreadyCommentQuery = "select exists(select * from ReviewComment where reviewId = ? and id = ?)" ;
        Object[] isAlreadyCommentParams = new Object[] {reviewId, userId};

        return this.jdbcTemplate.queryForObject(isAlreadyCommentQuery, int.class, isAlreadyCommentParams);
    }
}
