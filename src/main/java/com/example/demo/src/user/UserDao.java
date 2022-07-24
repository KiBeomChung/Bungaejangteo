package com.example.demo.src.user;


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
                "where Users.status = 'NORMAL' and Users.id = ?";
        int getUserInquiringParam = userId;

        return this.jdbcTemplate.query(getUserInquiringQuery,
                (rs, rowNum) -> new GetUserInquiringRes(
                        rs.getString("imageUrl"),
                        rs.getString("storeName"),
                        rs.getString("text"),
                        rs.getString("createdAt")),
                getUserInquiringParam);
    }
}
