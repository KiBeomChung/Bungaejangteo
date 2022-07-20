package com.example.demo.src.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FavoriteDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String addFollowing(int followingId){
        String addFollwingQuery = "insert into Following(followingId) values (?)"; // ? 를 팔로잉 하는것
        int addFollowingParam = followingId;
        this.jdbcTemplate.update(addFollwingQuery, addFollowingParam);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public String receiveFollowing(int followerId, int lastInsertId){
        String receiveFollowingQuery =  "insert into Follow(followerId, followingId) values(?, ?)";
        Object[] reveiveFollowingParams = new Object[]{followerId, lastInsertId};
        this.jdbcTemplate.update(receiveFollowingQuery, reveiveFollowingParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public int deleteFavorite(int followingId, int followerId){
        String deleteFollowingQuery = "delete F, FI\n" +
                "from Follow F inner join Following FI\n" +
                "where F.followingId = FI.id\n" +
                "and F.followerId = ? and FI.followingId = ?";
        Object[] deleteFollowingParams = new Object[]{followerId, followingId};

        return this.jdbcTemplate.update(deleteFollowingQuery, deleteFollowingParams);
    }

    public int checkUserStatus(int followingId){ //자신이 팔로우 하려는 상점이 신고 받지 않았는지 체크 하는 메소드
        String checkUserStatusQuery = "select exists(select id from Users where Users.id = ? AND Users.status ='NORMAL')";
        int checkUserStatusParam = followingId;
        return this.jdbcTemplate.queryForObject(checkUserStatusQuery, int.class, checkUserStatusParam);
    }

}
