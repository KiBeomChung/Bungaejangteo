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
}

