package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostReportReq;
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

    public int cancelLike(int userIdx, int productIdx){

        String cancelLikeQuery = "update Likes set status = 'INACTIVE' where userId = ? and productId = ?";
        Object[] cancelLikeParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.update(cancelLikeQuery, cancelLikeParams);

    }

    public int  createCollection(int userIdx, String collectionName) throws BaseException {
        String createCollectionQuery = "insert into LikeCollections (name,userId) VALUES (?,?)";
        Object[] createCollectionParams = new Object[]{collectionName,userIdx};
        return this.jdbcTemplate.update(createCollectionQuery, createCollectionParams);
    }

}
