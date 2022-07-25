package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.brand.model.GetBrandListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import com.example.demo.src.search.model.*;

import java.util.List;

@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetSearchWordRes> getSearchWord(int userIdx, String type) {
        String getSearchWordQuery = "";
        if (type.equals("recent")) {
            getSearchWordQuery = "SELECT id,searchWord FROM bungae.Searches where userIdx = ? and status = 'NORMAL' order by createdAt desc";
            return this.jdbcTemplate.query(getSearchWordQuery,
                    (rs, rowNum) -> new GetSearchWordRes(
                            rs.getInt("id"),
                            rs.getString("searchWord")
                    ), userIdx);

        } else {
            getSearchWordQuery = "select id,searchWord,count(*) from Searches group by searchWord order by count(*) desc limit 10";
            return this.jdbcTemplate.query(getSearchWordQuery,
                    (rs, rowNum) -> new GetSearchWordRes(
                            rs.getInt("id"),
                            rs.getString("searchWord")
                    ));
        }
    }

    public int deleteAllSearchs(int userIdx) throws BaseException {
        String deleteAllSearchsQuery = "update Searches set status = 'DELETED' where userIdx = ?";
        int result = this.jdbcTemplate.update(deleteAllSearchsQuery, userIdx);
        return result;
    }

    public int deleteSearch(int userIdx, int searchIdx) throws BaseException {
        String deleteAllSearchsQuery = "update Searches set status = 'DELETED' where userIdx = ? and id = ?";
        int result = this.jdbcTemplate.update(deleteAllSearchsQuery, new Object[]{userIdx, searchIdx});
        return result;
    }

    public int isCorrenctUser(int searchIdx) throws BaseException {
        String isCorrenctUserQuery = "select userIdx from Searches where id = ?";
        return this.jdbcTemplate.queryForObject(isCorrenctUserQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("userIdx")
                ), searchIdx);
    }

    public int isExistSearchIdx(int searchIdx) throws BaseException {
        return this.jdbcTemplate.queryForObject("select exists(select * from Searches where id = ? and status not in ('DELETED'))", int.class, searchIdx);
    }



    public List<GetSearchBrandRes> getSearchBrandList(int userId, int brandId) {

        String getSearchBrandQuery = "select Brands.imageUrl, Brands.name, Brands.englishName, exists(select * from BrandFollows inner join Brands on Brands.id = BrandFollows.brandId where BrandFollows.userId = ? and BrandFollows.brandId = ?) as isFollow\n" +
                "from Brands\n" +
                "where Brands.id = ? and Brands.status = 'NORMAL'";
        Object[] getSearchBrandParams = new Object[] {userId, brandId, brandId};

        String getProductsQuery = "select distinct Products.productId, ProductImages.imageUrl, Products.price, Products.name, Products.isSafePayment\n" +
                "from Products\n" +
                "inner join ProductTags on ProductTags.productId = Products.productId\n" +
                "inner join (SELECT imageUrl, productId FROM (SELECT * FROM ProductImages ORDER BY createdAt) a GROUP BY productId) ProductImages on Products.productId = ProductImages.productId\n" +
                "where Products.productId in (select productId from ProductTags where tag = ? or ?)";
//        String getSearchBrandQuery = "select distinct Products.id, Brands.imageUrl as brandImageUrl, Brands.name as brandName, Brands.englishName, exists(select * from BrandFollows inner join Brands on Brands.id = BrandFollows.brandId where BrandFollows.userId = ? and BrandFollows.brandId = ?) as isFollow,\n" +
//                "ProductImages.imageUrl, Products.price, Products.name, Products.isSafePayment,\n" +
//                "from Products\n" +
//                "inner join ProductTags on ProductTags.productId = Products.productId\n" +
//                "inner join Brands on Brands.name = (ProductTags.tag) or Brands.englishName = (ProductTags.tag)\n" +
//                "inner join (SELECT imageUrl, productId FROM (SELECT * FROM ProductImages ORDER BY createdAt) a GROUP BY productId) ProductImages on Products.productId = ProductImages.productId\n" +
//                "where Products.productId in (select productId from ProductTags where tag = ? or ?);";
        return this.jdbcTemplate.query(getSearchBrandQuery,
                (rs, rowNum) -> new GetSearchBrandRes(
                        rs.getString("imageUrl"),
                        rs.getString("name"),
                        rs.getString("englishName"),
                        rs.getInt("isFollow"),
                        this.jdbcTemplate.query(getProductsQuery,
                                (rs2, rowNum2) -> new GetProductInfo(
                                        rs2.getInt("productId"),
                                        rs2.getString("imageUrl"),
                                        rs2.getInt("price"),
                                        this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ? and status = 'NORMAL') as isLike",
                                                (rs3, rowNum3) -> new Integer(
                                                        rs3.getInt("isLike")),
                                                userId, rs2.getInt("productId")),
                                        rs2.getString("name"),
                                        rs2.getInt("isSafePayment"))
                                , new Object[]{rs.getString("name"), rs.getString("englishName")})
                ), getSearchBrandParams);
    }


    public int isDeletedUser(int userIdx) {
        String isDeletedUserQuery = "select exists(select * from Users where id = ? and status ='DELETED')";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, userIdx);
    }
}



