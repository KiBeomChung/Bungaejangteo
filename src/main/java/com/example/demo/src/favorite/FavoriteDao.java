package com.example.demo.src.favorite;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.GetFavoriteUserDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserProductsDetailRes;
import com.example.demo.src.favorite.model.GetFavoriteUserRes;
import com.example.demo.src.favorite.model.GetParamRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FavoriteDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String addFollowing(int followingId) {
        String addFollwingQuery = "insert into Following(followingId) values (?)"; // ? 를 팔로잉 하는것
        int addFollowingParam = followingId;
        this.jdbcTemplate.update(addFollwingQuery, addFollowingParam);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public String receiveFollowing(int followerId, int lastInsertId) {
        String receiveFollowingQuery = "insert into Follow(followerId, followingId) values(?, ?)";
        Object[] reveiveFollowingParams = new Object[]{followerId, lastInsertId};
        this.jdbcTemplate.update(receiveFollowingQuery, reveiveFollowingParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, String.class);
    }

    public int deleteFavorite(int followingId, int followerId) {
        String deleteFollowingQuery = "delete F, FI\n" +
                "from Follow F inner join Following FI\n" +
                "where F.followingId = FI.id\n" +
                "and F.followerId = ? and FI.followingId = ?";
        Object[] deleteFollowingParams = new Object[]{followerId, followingId};

        return this.jdbcTemplate.update(deleteFollowingQuery, deleteFollowingParams);
    }

    public int checkUserStatus(int followingId) { //자신이 팔로우 하려는 상점이 신고 받지 않았는지 체크 하는 메소드
        String checkUserStatusQuery = "select exists(select id from Users where Users.id = ? AND Users.status ='NORMAL')";
        int checkUserStatusParam = followingId;
        return this.jdbcTemplate.queryForObject(checkUserStatusQuery, int.class, checkUserStatusParam);
    }

    public List<GetFavoriteUserRes> getFavoriteUserDetailList(int userId) {
        String getFavoriteUserQuery = "select Users.id, Users.ImageUrl, Users.storeName, count(distinct Following.id) as followerNum, count(distinct Products.productId) as productsNum\n" +
                "from Users\n" +
                "inner join Following on Users.id = Following.followingId\n" +
                "left join Products on Products.userId = Users.id\n" +
                "inner join Follow on Follow.FollowingId = Following.id\n" +
                "where Following.followingId in (\n" +
                "    select Following.followingId\n" +
                "from Following\n" +
                "inner join Follow on Follow.followingId = Following.id\n" +
                "where Follow.followerId = ?\n" +
                "      )\n" +
                "group by storeName";
        int getFavoirteUserParam = userId;
        return this.jdbcTemplate.query(getFavoriteUserQuery,
                (rs, rowNum) -> new GetFavoriteUserRes(
                        rs.getInt("id"),
                        rs.getString("imageUrl"),
                        rs.getString("storeName"),
                        rs.getInt("followerNum"),
                        rs.getInt("productsNum")),
                getFavoirteUserParam);
    }

    public List<GetFavoriteUserProductsDetailRes> getFollowStoreImage(int id, int userId) {
        String getFollowStoreImageQuery = "select ProductImages.imageUrl, Products.price\n" +
                "from Products\n" +
                "left join ProductImages on ProductImages.productId = Products.productId\n" +
                "inner join Following on Following.followingId = Products.userId and Following.followingId = ?\n" +
                "inner join Follow on Follow.followingId = Following.id and Follow.followerId = ?";
        Object[] getFollowStoreImageParams = new Object[]{id, userId};

        return this.jdbcTemplate.query(getFollowStoreImageQuery,
                (rs, rowNum) -> new GetFavoriteUserProductsDetailRes(
                        rs.getString("imageUrl"),
                        rs.getInt("price")),
                getFollowStoreImageParams);
    }

//    public List<GetFavoriteUserProductsDetailRes> getFavoriteUserProductsDetailList (int userId) {
//        String getFavoriteUserProductsDetailQuery = "";
//        int getFavoritedUserProductsDetailParam = userId;
//
//
//    }

//    public List<GetFavoriteUserDetailRes> getFavoriteUserDetailList(int userId) {
//        String getFavoriteUserDetailQuery = "select Users.ImageUrl, Users.storeName, count(distinct Following.id) as followerNum, count(distinct Products.productId) as productsNum\n" +
//                "from Users\n" +
//                "inner join Following on Users.id = Following.followingId\n" +
//                "left join Products on Products.userId = Users.id\n" +
//                "inner join Follow on Follow.FollowingId = Following.id\n" +
//                "where Following.followingId in (\n" +
//                "    select Following.followingId\n" +
//                "from Following\n" +
//                "inner join Follow on Follow.followingId = Following.id\n" +
//                "where Follow.followerId = ?\n" +
//                "      )\n" +
//                "group by storeName";
//
//        String getParamsQuery = "select Following.followingId\n" +
//                "from Following\n" +
//                "inner join Follow on Follow.followingId = Following.id\n" +
//                "where Follow.followerId = ?";
//        int getParam = userId;
//        List<GetParamRes> getParams = this.jdbcTemplate.query(getParamsQuery,
//                (rs, rowNum) -> new GetParamRes(
//                        rs.getInt("followingId")
//                ), getParam);


//        System.out.println("!!!!!!" + getParams.get(0).getFollowingId());
//        System.out.println("!!!!!!" + getParams.get(1).getFollowingId());


//        String getFavoriteUserProductsDetailQuery = "select Products.userId, ProductImages.imageUrl, Products.price\n" +
//                "from ProductImages\n" +
//                "inner join Products on Products.productId = ProductImages.productId\n"  +
//                "inner Join Following on Following.followingId = Products.userId\n" +
//                "where Following.followingId = Products.userId\n" +
//                "group by Products.userId, Products.productId";
//        int getFavoriteUserDetailParam = userId;
//        int getFavoriteUserProductsDetailParam = userId;
//        return this.jdbcTemplate.query(getFavoriteUserDetailQuery,
//                (rs, rowNum) -> new GetFavoriteUserDetailRes(
//                        rs.getString("imageUrl"),
//                        rs.getString("storeName"),
//                        rs.getInt("followerNum"),
//                        rs.getInt("productsNum"),
//                        this.jdbcTemplate.query(getFavoriteUserProductsDetailQuery,
//                                (rs1, rowNum1) -> new GetFavoriteUserProductsDetailRes(
//                                        rs1.getString("imageUrl"),
//                                        rs1.getInt("price"))
//                        )
//                ),
//                getFavoriteUserDetailParam);
//    }

//    public List<GetFavoriteUserProductsDetailRes> getFavoriteUserProductsDetailList(int userId) {
//        String getFavoriteUserProductsDetailQuery = "select distinct ProductImages.imageUrl, Products.price\n" +
//                "from ProductImages\n" +
//                "inner join Products on Products.productId = ProductImages.productId\n" +
//                "inner Join Following on Following.followingId = Products.userId\n" +
//                "where Following.followingId in (\n" +
//                "    select Following.followingId\n" +
//                "    from Following\n" +
//                "    inner join Follow on Follow.followingId = Following.id\n" +
//                "where Follow.followerId = ?)";
//        int getFavoriteUserProductsDetailParam = userId;
//        return this.jdbcTemplate.query(getFavoriteUserProductsDetailQuery,
//                (rs, rowNum) -> new GetFavoriteUserProductsDetailRes(
//                        rs.getString("imageUrl"),
//                        rs.getInt("price")),
//                getFavoriteUserProductsDetailParam);
//    }

    public int addFollowBrand(int userId, int brandId) {
        String addFollowBrandQuery = "insert into BrandFollows(userId, brandId)\n" +
                "(\n" +
                "    select ?, Brands.id\n" +
                "    from Brands\n" +
                "    where Brands.status = 'NORMAL' and Brands.id = ?\n" +
                ")";
        Object[] addFollowBrandParams = new Object[]{userId, brandId};
        return this.jdbcTemplate.update(addFollowBrandQuery, addFollowBrandParams);
    }

    public int duplicatedFollow(int userId, int brandId) {
        String duplicatedFollowQuery = "select exists (select BrandFollows.id from BrandFollows where userId = ? and brandId = ?);";
        Object[] duplicatedFollowParams = new Object[]{userId, brandId};

        return this.jdbcTemplate.queryForObject(duplicatedFollowQuery, int.class, duplicatedFollowParams);
    }

    public int isExistBrand(int brandId) {
        String isExistBrandQuery = "select exists (select Brands.id from Brands where Brands.id = ?\n)";
        //Object[] isExistBrandParam = new Object[] {brandId, brandId};
        int isExistBrandParam = brandId;

        return this.jdbcTemplate.queryForObject(isExistBrandQuery, int.class, isExistBrandParam);
    }

    public int isDeletedBrand(int brandId) {
        String isDeletedBrandQuery = "select exists(select Brands.id from Brands where Brands.id = ? and Brands.status = 'DELETED')";
        int isDeletedBrandParam = brandId;

        return this.jdbcTemplate.queryForObject(isDeletedBrandQuery, int.class, isDeletedBrandParam);
    }

    public int deleteFollowBrand(int userId, int brandId) {
        String deleteFollowBrandQuery = "delete from BrandFollows where BrandFollows.userId = ? and BrandFollows.brandId = ?";
        Object[] deleteFollowBrandParams = new Object[]{userId, brandId};

        return this.jdbcTemplate.update(deleteFollowBrandQuery, deleteFollowBrandParams);
    }
}
