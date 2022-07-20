package com.example.demo.src.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LikeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createLike(int userIdx, int productIdx){
        if (searchLikes(userIdx,productIdx) <= 0){
            String createLikeQuery = "insert into Likes (userId,productId) VALUES (?,?)";
            Object[] createLikeParams = new Object[]{userIdx,productIdx};
            return this.jdbcTemplate.update(createLikeQuery, createLikeParams);

        }else{
            String createLikeQuery = "update Likes set status = 'NORMAL' where userId = ? and productId = ?";
            Object[] createLikeParams = new Object[]{userIdx,productIdx};
            return this.jdbcTemplate.update(createLikeQuery, createLikeParams);
        }

    }

    public int searchLikes(int userIdx, int productIdx){
        Object[] createLikeParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.queryForObject("select count(*) from Likes where userId = ? and productId = ?",
                (rs, rowNum) -> new Integer(
                        rs.getInt("count(*)")),createLikeParams);

    }
}
