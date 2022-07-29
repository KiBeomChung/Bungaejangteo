package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetCollectionProductsRes;
import com.example.demo.src.like.model.GetCollectionRes;
import com.example.demo.src.like.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
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
    @Transactional
    public int  deleteCollection(int collectionIdx) throws BaseException {
        String deleteCollectionQuery = "update LikeCollections set status = 'DELETED' where id = ?";
        int result = this.jdbcTemplate.update(deleteCollectionQuery, collectionIdx);

        String deleteCollectionProductQuery = "update Likes set collectionId = null where collectionId = ?";
        int result2 = this.jdbcTemplate.update(deleteCollectionProductQuery, collectionIdx);

        return result + result2;
    }

    public GetLikesRes getLikes(int userIdx, String order, String status) {
        String GetProductsQuery = "select Products.productId,b.imageUrl as productImageUrl,Products.name as productName,price,Users.id,Users.imageUrl as storeImageUrl,storeName,isSafePayment,Likes.status,Products.status as productStatus,Likes.createdAt\n" +
                "                from (SELECT imageUrl, productId FROM (SELECT * FROM ProductImages ORDER BY createdAt)a GROUP BY productId)b\n" +
                "                inner join Products on b.productId = Products.productId\n" +
                "                inner join Users on Products.userId = Users.id\n" +
                "                inner join Likes on Products.productId = Likes.productId\n" +
                "                where Likes.userId = ? and Likes.status = 'NORMAL' and Likes.collectionId IS NULL \n" +
                "                and Products.status not in ('DELETED')";

        String GetCollectionsQuery = "select LikeCollections.id ,name,count(*) as productsNum\n" +
                "from LikeCollections inner join Likes on Likes.collectionId = LikeCollections.id \n" +
                "where LikeCollections.userId = ? and LikeCollections.status = 'NORMAL'\n" +
                "group by LikeCollections.id";

        if(status.equals("sale")){
            if(order.equals("")){
                GetProductsQuery += " and Products.status = 'SALE'";
            }else if(order.equals("new")){
                GetProductsQuery += " and Products.status = 'SALE' order by Likes.createdAt desc";
            }else if(order.equals("past")){
                GetProductsQuery += " and Products.status = 'SALE' order by Likes.createdAt";
            }else if(order.equals("hot")){
                GetProductsQuery += " and Products.status = 'SALE' order by (select count(*) from Likes where productId = Products.productId)";
            }else if(order.equals("low")){
                GetProductsQuery += " and Products.status = 'SALE' order by Products.price";
            }else if(order.equals("high")){
                GetProductsQuery += " and Products.status = 'SALE' order by Products.price desc";
            }
        }else if (status.equals("not-sale")){
            if(order.equals("")){
                GetProductsQuery += " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT'";
            }else if(order.equals("new")){
                GetProductsQuery += " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT' order by Likes.createdAt desc";
            }else if(order.equals("past")){
                GetProductsQuery += " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT' order by Likes.createdAt";
            }else if(order.equals("hot")){
                GetProductsQuery += " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT' order by (select count(*) from Likes where productId = Products.productId)";
            }else if(order.equals("low")){
                GetProductsQuery += " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT' order by Products.price";
            }else if(order.equals("high")){
                GetProductsQuery += " and Products.status = 'RESERVE' or Products.status = 'SOLDOUT' order by Products.price desc";
            }

        }else{
            if(order.equals("")){
              //전체 조회
            }else if(order.equals("new")){
                GetProductsQuery += " order by Likes.createdAt desc";
            }else if(order.equals("past")){
                GetProductsQuery += " order by Likes.createdAt";
            }else if(order.equals("hot")){
                GetProductsQuery += " and Products.status = 'SALE' order by (select count(*) from Likes where productId = Products.productId)";
            }else if(order.equals("low")){
                GetProductsQuery += " order by Products.price";
            }else if(order.equals("high")){
                GetProductsQuery += " order by Products.price desc";
            }
        }

        List<GetCollectionRes> collectionResult =
                this.jdbcTemplate.query(GetCollectionsQuery,
                (rs, rowNum) -> new GetCollectionRes(
                        rs.getInt("LikeCollections.id"),
                        this.jdbcTemplate.query("select Likes.productId,ProductImages.imageUrl\n" +
                                        "from Likes inner join LikeCollections on Likes.collectionId = LikeCollections.id\n" +
                                        "inner join ProductImages on ProductImages.productId = Likes.productId\n" +
                                        "where Likes.collectionId = ? and LikeCollections.userId = ? limit 4",
                                (rs2, rowNum2) -> new String(
                                        rs2.getString("ProductImages.imageUrl")
                                ),new Object[]{rs.getInt("LikeCollections.id"),userIdx}),
                        rs.getString("name"),
                        rs.getInt("productsNum")
                ),userIdx);


        List<GetCollectionProductsRes> productResult =
                        this.jdbcTemplate.query(GetProductsQuery,
                                (rs, rowNum) -> new GetCollectionProductsRes(
                                        rs.getInt("Products.productId"),
                                        rs.getString("productImageUrl"),
                                        rs.getString("productName"),
                                        rs.getInt("price"),
                                        rs.getInt("Users.id"),
                                        rs.getString("storeImageUrl"),
                                        rs.getString("storeName"),
                                        rs.getInt("isSafePayment"),
                                        rs.getString("productStatus")
                                ),userIdx);

        return new GetLikesRes(collectionResult,productResult);
    }
    public int isDeletedUser(int userIdx) {
        String isDeletedUserQuery = "select exists(select * from Users where id = ? and status ='DELETED')";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, userIdx);
    }

    public int isExistLike(int userIdx,int productIdx) {
        String isDeletedUserQuery = "select exists(select * from Likes where userId = ? and productId = ?)";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, userIdx,productIdx);
    }

    public int isExistCanceledLike(int userIdx,int productIdx) {
        String isDeletedUserQuery = "select exists(select * from Likes where userId = ? and productId = ? and status = 'DELETED')";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, userIdx,productIdx);
    }

    public int isDeletedCollections(int userIdx,int collectionIdx) {
        String isDeletedUserQuery = "select exists(select * from LikeCollections where id = ? and userId = ? and status = 'DELETED')";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, collectionIdx,userIdx);
    }

    public int isExistColloectionIdx(int collectionIdx) {
        String isDeletedUserQuery = "select exists(select * from LikeCollections where id = ?)";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, collectionIdx);
    }



}
