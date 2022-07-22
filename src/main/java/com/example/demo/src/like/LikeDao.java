package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetCollectionProductsRes;
import com.example.demo.src.like.model.PostCollectionProductReq;
import com.example.demo.src.product.model.GetProductRes;
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

    public List<GetCollectionProductsRes> getCollectionProducts(int userIdx, int collectionIdx, String status) {
        String GetProductQuery = "select Products.productId,b.imageUrl as productImageUrl,Products.name as productName,price,Users.id,Users.imageUrl as storeImageUrl,storeName,isSafePayment,Products.status\n" +
                "from (SELECT imageUrl, productId FROM (SELECT * FROM ProductImages ORDER BY createdAt)a GROUP BY productId)b\n" +
                "inner join Products on b.productId = Products.productId\n" +
                "inner join Users on Products.userId = Users.id\n" +
                "where Products.productId in (select productId from Likes where collectionId = ? and userId = ?)";

        if(status.equals("sale")){
            GetProductQuery = GetProductQuery + " and Products.status = 'SALE'";
        }else if (status.equals("not-sale")){
            GetProductQuery = GetProductQuery + " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT'";
        }

        Object[] createCollectionParams = new Object[]{collectionIdx,userIdx};
        return this.jdbcTemplate.query(GetProductQuery,
                (rs, rowNum) -> new GetCollectionProductsRes(
                        rs.getInt("Products.productId"),
                        rs.getString("productImageUrl"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getInt("Users.id"),
                        rs.getString("storeImageUrl"),
                        rs.getString("storeName"),
                        rs.getInt("isSafePayment"),
                        rs.getString("Products.status")
                ),createCollectionParams);
    }

    public int  deleteCollection(int collectionIdx) throws BaseException {
        String deleteCollectionQuery = "update LikeCollections set status = 'DELETED' where id = ?";
        int result = this.jdbcTemplate.update(deleteCollectionQuery, collectionIdx);

        String deleteCollectionProductQuery = "update Likes set collectionId = null where collectionId = ?";
        int result2 = this.jdbcTemplate.update(deleteCollectionProductQuery, collectionIdx);

        return result + result2;
    }



}
