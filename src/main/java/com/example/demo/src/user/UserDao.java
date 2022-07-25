package com.example.demo.src.user;


import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers() {
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
        );
    }

    public List<GetUserRes> getUsersByEmail(String email) {
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx) {
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }


    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into Users (name, isCertified, phoneNum) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), 1,postUserReq.getPhoneNum()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    public int checkExisttUser(String phoneNum) {
        String checkphoneNumQuery = "select exists(select phoneNum from Users where phoneNum = ?)";
        String checkphoneNumParams = phoneNum;
        return this.jdbcTemplate.queryForObject(checkphoneNumQuery,
                int.class,
                checkphoneNumParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq) {
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery, modifyUserNameParams);
    }

    public Integer getUserIdx(PostUserReq postUserReq) {
        String getUserIdxQuery = "select id from Users where phoneNum = ?";
        String getUserIdxParams = postUserReq.getPhoneNum();

        return this.jdbcTemplate.queryForObject(getUserIdxQuery,
                int.class,
                getUserIdxParams);

    }

    public int createStoreName(int userIdx,String storeName) {
        String createStoreNameQuery = "update Users set storeName = ? where id = ? ";
        Object[] createStoreNameParams = new Object[]{storeName, userIdx};

        return this.jdbcTemplate.update(createStoreNameQuery, createStoreNameParams);
    }

    public int checkExistStoreName(String storeName) {
        String checkStoreNameQuery = "select exists(select storeName from Users where storeName = ?)";
        String checkStoreNameParams = storeName;
        return this.jdbcTemplate.queryForObject(checkStoreNameQuery,
                int.class,
                checkStoreNameParams);

    }

    public int modifyStoreInfo(PatchUserStoreInfoReq patchUserStoreInfoReq, int id){
        String modifyStroeInfoQuery = "update Users set storeName = ?,\n" +
                "shopUrl = ?,\n" +
                "contactTime = ?,\n " +
                "description = ?,\n" +
                "policy = ?,\n" +
                "precautions = ?\n" +
                "where id = ?";
        int modifyStoreIdParam = id;
        Object[] modifyStoreInfoParams = new Object[]{patchUserStoreInfoReq.getStoreName(), patchUserStoreInfoReq.getShopUrl(),
                patchUserStoreInfoReq.getContactTime(), patchUserStoreInfoReq.getDescription(), patchUserStoreInfoReq.getPolicy(),
                patchUserStoreInfoReq.getPrecautions(), modifyStoreIdParam};

        return this.jdbcTemplate.update(modifyStroeInfoQuery, modifyStoreInfoParams);

    }

    public GetUserStoreInfoRes getUserStoreInfo(int id) {
        String getUserStoreInfoQuery = "select Users.imageUrl,\n" +
                "Users.storeName,\n" +
                "Users.shopUrl,\n" +
                "Users.contactTime,\n" +
                "Users.description,\n" +
                "Users.policy,\n" +
                "Users.precautions\n" +
                "from Users\n" +
                "where id = ?;";
        int getUserStoreInfoParam = id;
        return this.jdbcTemplate.queryForObject(getUserStoreInfoQuery,
                (rs, rowNum) -> new GetUserStoreInfoRes(
                        rs.getString("imageUrl"),
                        rs.getString("storeName"),
                        rs.getString("shopUrl"),
                        rs.getString("contactTime"),
                        rs.getString("description"),
                        rs.getString("policy"),
                        rs.getString("precautions")),
                getUserStoreInfoParam);
    }

    public int checkId(int id){
        String checkIdQuery = "select exists(select id from Users where id = ? and status = 'NORMAL')";
        int checkIdParam = id;
        return this.jdbcTemplate.queryForObject(checkIdQuery, int.class, checkIdParam);
    }

    public int modifyProductState(PatchProductStateReq patchProductStateReq, int userId, int productsId){
        String modifyProductStateQuery = "update Products inner join Users\n" +
                "on Products.userId = Users.id\n" +
                "set Products.status = ?\n" +
                "where Users.id = ? and Products.productId = ?";

        Object[] modifyProductStateParam = new Object[]{patchProductStateReq.getStatus(), userId, productsId};
        return this.jdbcTemplate.update(modifyProductStateQuery, modifyProductStateParam);
    }

    public int checkProductStateReport(int productId){
        String checkProductStateQuery ="select exists(select Products.productId from Products\n" +
                "inner join ProductReports on ProductReports.productId = Products.productId\n" +
                "and Products.productId = ?\n" +
                "where ProductReports.status = 'COMPLETED')";
        int checkProductStateParam = productId;
        return this.jdbcTemplate.queryForObject(checkProductStateQuery, int.class, checkProductStateParam);
    }

    public int checkProductStateDelete(int productsId) {
        String checkProductStateDeleteQuery = "select exists(select productId from Products where productId = ? and status ='DELETED')";
        int checkProductStateDeleteParam = productsId;
        return this.jdbcTemplate.queryForObject(checkProductStateDeleteQuery, int.class, checkProductStateDeleteParam);
    }

    public GetUserInfoRes getUserInfo(int userId) {
        String getUserInfoQuery = "select Users.gender, Users.birth, Users.phoneNum, Users.authMethod\n" +
                "from Users\n" +
                "where Users.status = 'NORMAL' and Users.id = ?";
        int getUserInfoParam = userId;
        return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (rs, rowNum) -> new GetUserInfoRes(
                        rs.getString("gender"),
                        rs.getString("birth"),
                        rs.getString("phoneNum"),
                        rs.getString("authMethod")
                ), getUserInfoParam);
    }

    public int checkUserState(int userId) {
        String checkUserStateQuery = "select exists(select id from Users where id = ? and status ='NORMAL')";
        int checkUserStateParam = userId;
        return this.jdbcTemplate.queryForObject(checkUserStateQuery, int.class, checkUserStateParam);
    }

    public int modifyUserInfo(PatchUserInfoReq patchUserInfoReq, int userId) {

        String modifyUserInfoQuery = "update Users set gender = ?, birth = ?, phoneNum = ? where id = ?\n";
        Object[] modifyUserInfoParams = new Object[]{patchUserInfoReq.getGender(), patchUserInfoReq.getBirth(),
                patchUserInfoReq.getPhoneNum(), userId};

        return this.jdbcTemplate.update(modifyUserInfoQuery, modifyUserInfoParams);
    }

    public String addInquiring(int inquiringId, PostUserInquiryReq postUserInquiryReq) {
        String addInquiringQuery = "insert into Inquiring(inquiringId, text) values (?, ?)";
        Object[] addInquiringParams = new Object[]{inquiringId, postUserInquiryReq.getText()};

        this.jdbcTemplate.update(addInquiringQuery, addInquiringParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public int addInquired(int inquiredId, int connectId) {
        String addInquiredQuery = "insert into Inquired(inquiredId, connectId) values(?, ?)";
        Object[] addInquiredParams = new Object[] {inquiredId, connectId};

        return this.jdbcTemplate.update(addInquiredQuery, addInquiredParams);
    }

    public List<GetUserInquiringRes> getUserInquiring(int userId) {
        String getUserInquiringQuery = "select Users.imageUrl, Users.storeName, Inquiring.text \n," +
                "CASE WHEN timestampdiff(second,Inquiring.createdAt, current_timestamp) < 60\n" +
                "                THEN concat(timestampdiff(second,Inquiring.createdAt, current_timestamp), ' 초 전')\n" +
                "            WHEN timestampdiff(minute, Inquiring.createdAt, current_timestamp) < 60\n" +
                "                THEN concat(timestampdiff(minute, Inquiring.createdAt, current_timestamp), ' 분 전')\n" +
                "            WHEN timestampdiff(hour,Inquiring.createdAt, current_timestamp) < 24\n" +
                "                THEN concat(timestampdiff(hour,Inquiring.createdAt, current_timestamp), ' 시간 전')\n" +
                "            WHEN timestampdiff(day,Inquiring.createdAt, current_timestamp) < 7\n" +
                "                THEN concat(timestampdiff(day,Inquiring.createdAt, current_timestamp), ' 일 전')\n" +
                "            WHEN timestampdiff(week,Inquiring.createdAt, current_timestamp) < 4\n" +
                "                THEN concat(timestampdiff(week,Inquiring.createdAt, current_timestamp), ' 주 전')\n" +
                "            WHEN timestampdiff(month,Inquiring.createdAt, current_timestamp) < 12\n" +
                "                THEN concat(timestampdiff(month,Inquiring.createdAt, current_timestamp), ' 달 전')\n" +
                "            WHEN timestampdiff(year,Inquiring.createdAt, current_timestamp) < 1000\n" +
                "                THEN concat(timestampdiff(year,Inquiring.createdAt, current_timestamp), ' 년 전')\n" +
                "           END AS 'createdAt'\n" +
                "from Users\n" +
                "inner join Inquiring on Inquiring.inquiringId = Users.id\n" +
                "inner join Inquired on Inquired.connectId = Inquiring.id\n" +
                "where Inquired.inquiredId = ? and Users.status = 'NORMAL'";
        int getUserInquiringParam = userId;

        return this.jdbcTemplate.query(getUserInquiringQuery,
                (rs, rowNum) -> new GetUserInquiringRes(
                        rs.getString("imageUrl"),
                        rs.getString("storeName"),
                        rs.getString("text"),
                        rs.getString("createdAt")),
                getUserInquiringParam);
    }

    public int addVisitNum(int userId) {
        String addVisitNumQuery = "update Users set Users.visitNum = visitNum + 1 where Users.id = ? and Users.status = 'NORMAL'";
        int addVisitNumParam = userId;

        return this.jdbcTemplate.update(addVisitNumQuery, addVisitNumParam);
    }

    public GetAnotherUserStoreInfoRes getAnotherUserStoreInfo(int userId, int targetId) {

        String getAnotherUserStoreProductInfoQuery = "select Products.productId, ProductImages.imageUrl, Products.price, Products.name\n" +
                "from Products inner join (SELECT imageUrl, productId FROM (SELECT * FROM ProductImages ORDER BY createdAt)a GROUP BY productId) ProductImages on Products.productId = ProductImages.productId\n" +
                "where Products.userId = ?";

        String getAnotherUserStoreInfoQuery = "select Users.imageUrl, Users.storeName, (select round(avg(Review.reviewScore), 2) from Review inner join Sell on Sell.reviewId = Review.reviewId and Sell.id = ?) as reviewAvg,\n" +
                "(select count(Review.reviewId) from Review inner join Sell on Sell.reviewId = Review.reviewId and Sell.id = ?) as reviewNum,\n" +
                "       timestampdiff(day, Users.createdAt, current_timestamp) as openDate,\n" +
                "       Users.visitNum, (select count(Products.productId) from Products where Products.userId = ?) as productNum,\n" +
                "       (select count(Review.reviewId) from Review inner join Sell on Sell.reviewId = Review.reviewId and Sell.id = ?) as reviewNum2,\n" +
                "       (select count(Follow.id) from Follow inner join Following on Follow.followingId = Following.id and Follow.followerId = ?) as followNum,\n" +
                "       (select count(Following.id) from Following inner join Follow on Follow.followingId = Following.id and Following.followingId = ?) as followingNum,\n" +
                "       Users.isCertified,\n" +
                "       (select count(Sell.sellId) from Sell inner join Users on Users.id = Sell.id and Users.id = ?) as sellNum,\n" +
                "       Users.contactTime, Users.description, Users.policy, exists(select * from Follow inner join Following on Follow.followingId = Following.id and Following.followingId = ? and Follow.followerId = ?) as isFollow\n" +
                "from Users\n" +
                "where Users.id = ? limit 10";
        Object[] getAnotherUserStoreInfoParams = new Object[] {targetId,targetId,targetId,targetId,targetId,targetId,targetId, targetId ,userId, targetId};

        return this.jdbcTemplate.queryForObject(getAnotherUserStoreInfoQuery,
                (rs, rowNum) -> new GetAnotherUserStoreInfoRes(
                        rs.getString("imageUrl"),
                        rs.getString("storeName"),
                        rs.getDouble("reviewAvg"),
                        rs.getInt("openDate"),
                        rs.getInt("visitNum"),
                        rs.getInt("productNum"),
                        rs.getInt("reviewNum2"),
                        rs.getInt("followNum"),
                        rs.getInt("followingNum"),
                        rs.getInt("isCertified"),
                        rs.getInt("sellNum"),
                        rs.getString("contactTime"),
                        rs.getString("description"),
                        rs.getString("policy"),
                        this.jdbcTemplate.query(getAnotherUserStoreProductInfoQuery,
                               (rs2, rowNum2) -> new GetAnotherUserStoreProductInfoRes(
                                       rs2.getInt("productId"),
                                       rs2.getString("imageUrl"),
                                       rs2.getInt("price"),
                                       rs2.getString("name"),
                                       this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ? and status = 'NORMAL') as b",
                                               (rs3, rowNum3) -> new Integer(
                                                       rs3.getInt("b")),
                                                       targetId, rs2.getInt("productId"))
                               ), targetId), rs.getInt("isFollow"))
                , getAnotherUserStoreInfoParams);
    }

    public int deleteInquiring(int inquiredId, int inquiringId, PatchUserDeleteInqReq patchUserDeleteInqReq) {
        String deleteInquiringQuery = "update Inquiring a inner join Inquired b on a.id = b.connectId\n" +
                "set a.status = ?, b.status = ?\n" +
                "where a.inquiringId = ? and b.inquiredId = ?";

        Object[] deleteInquiringParams = new Object[] {patchUserDeleteInqReq.getStatus(), patchUserDeleteInqReq.getStatus(), inquiringId, inquiredId};

        return this.jdbcTemplate.update(deleteInquiringQuery, deleteInquiringParams);
    }

    public List<GetProductRes> getMyPageProducts(int userIdx, String status) {
        String GetProductQuery = "SELECT Products.productId,name,price,region,isSafePayment,\n" +
                "case \n" +
                "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전') \n" +
                "\twhen (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전') \n" +
                "\twhen (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전') \n" +
                "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전') \n" +
                "END AS elapsedTime,\n" +
                "case \n" +
                "    when (likeCount is null) then 0\n" +
                "    when (likeCount is not null) then likeCount\n" +
                "end as likeCount, exists(select * from Likes where userId = 1 AND productId = Products.productId) as isExist,imageUrl\n" +
                "from (Products left outer join (SELECT imageUrl, productId\n" +
                "                FROM (SELECT *\n" +
                "                       FROM ProductImages\n" +
                "                       ORDER BY createdAt)b\n" +
                "                 GROUP BY productId\n" +
                "                 )as imageTable\n" +
                "                 on imageTable.productId = Products.productId)\n" +
                "left outer join (select productId,count(*) as likeCount from Likes group by productId)as c\n" +
                "on Products.productId =  c.productId\n" +
                "where Products.userId = ? " ;
        if(status.equals("sale")){
            GetProductQuery += " and Products.status = 'SALE' order by Products.createdAt desc";
        }else if(status.equals("reserve")){
            GetProductQuery += " and Products.status = 'RESERVE' order by Products.createdAt desc";
        }else{//sold-out
            GetProductQuery += " and Products.status = 'SOLDOUT' order by Products.createdAt desc";
        }

        return this.jdbcTemplate.query(GetProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("Products.productId"),
                        rs.getString("imageUrl"),
                        rs.getInt("isExist"),
                        rs.getInt("price"),
                        rs.getString("name"),
                        rs.getString("region"),
                        rs.getString("elapsedTime"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("likeCount")
                ),userIdx);
    }
}
