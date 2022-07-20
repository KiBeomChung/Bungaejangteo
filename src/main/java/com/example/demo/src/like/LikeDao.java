package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.PostCollectionProductReq;
import com.example.demo.src.product.model.PostReportReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

        String cancelLikeQuery = "update Likes set status = 'DELETED' where userId = ? and productId = ?";
        Object[] cancelLikeParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.update(cancelLikeQuery, cancelLikeParams);

    }

    public int  createCollection(int userIdx, String collectionName) throws BaseException {
        String createCollectionQuery = "insert into LikeCollections (name,userId) VALUES (?,?)";
        Object[] createCollectionParams = new Object[]{collectionName,userIdx};
        return this.jdbcTemplate.update(createCollectionQuery, createCollectionParams);
    }

    public int  updateCollection(int collectionIdx, String collectionName) throws BaseException {
        String updateCollectionQuery = "update LikeCollections set name = ? where id = ?";
        Object[] updateCollectionParams = new Object[]{collectionName,collectionIdx};
        return this.jdbcTemplate.update(updateCollectionQuery, updateCollectionParams);
    }

    public int  createCollectionProduct(int userIdx, int collectionIdx, List<Integer> productIdxList) throws BaseException {

        int result = 0;
        for (int i = 0 ; i < productIdxList.size(); i++){
            String createCollectionQuery = "update Likes set collectionId = ? where userId = ? and productId = ?";
            Object[] createCollectionParams = new Object[]{collectionIdx,userIdx,productIdxList.get(i)};
            result += this.jdbcTemplate.update(createCollectionQuery, createCollectionParams);
        }
        return result;

    }

}
