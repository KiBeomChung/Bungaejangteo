package com.example.demo.src.brand;

import com.example.demo.src.product.model.GetProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;

import com.example.demo.src.brand.model.*;
import java.util.List;

@Repository
public class BrandDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetBrandListRes> getBrandList(int userIdx,String order,String follow) {
        String getBrandQuery = "select Brands.id,name,englishName,imageUrl,userId,exists(select * from BrandFollows where userId = ? and brandId = Brands.id)as isExist,productNum\n" +
                "from Brands left outer join BrandFollows on Brands.id = BrandFollows.brandId \n" +
                "left outer join (select tag,count(*)as productNum\n" +
                "from ProductTags group by tag)b on name = tag or englishName = tag\n" +
                "group by name" ;
        if(order.equals("") || order.equals("korean")){
            if(follow.equals("")){
                getBrandQuery+= " order by (case name when name between '가' and '힣' then 2 else 1 end)asc, name asc";
            }else if(follow.equals("true")){
                getBrandQuery += " having isExist = 1 order by (case name when name between '가' and '힣' then 2 else 1 end)asc, name asc";
            }else if(follow.equals("false")){
                getBrandQuery += " order by (case name when name between '가' and '힣' then 2 else 1 end)asc, name asc";
            }
        }else if(order.equals("english")){
            if(follow.equals("")){
                getBrandQuery += " order by englishName";
            }else if(follow.equals("true")){
                getBrandQuery += " having isExist = 1 order by englishName";
            }else if(follow.equals("false")){
                getBrandQuery += " order by englishName";
            }
        }
        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new GetBrandListRes(
                        rs.getInt("Brands.id"),
                        rs.getString("name"),
                        rs.getString("englishName"),
                        rs.getInt("productNum"),
                        rs.getBoolean("isExist"),
                        rs.getString("imageUrl")
                ),userIdx);
    }

    public List<GetBrandListRes> getSearchBrandList(int userIdx,String searchWord) {
        String getBrandQuery = "select Brands.id,name,englishName,imageUrl,userId,exists(select * from BrandFollows where userId = ? and brandId = Brands.id)as isExist,productNum\n" +
                "from Brands left outer join BrandFollows on Brands.id = BrandFollows.brandId \n" +
                "left outer join (select tag,count(*)as productNum\n" +
                "from ProductTags group by tag)b on name = tag or englishName = tag\n" +
                "where name like ? OR englishName like ? \n"+
                "group by name order by (case name when name between '가' and '힣' then 2 else 1 end)asc, name asc" ;

        System.out.println("'"+searchWord+"'");
        System.out.println(getBrandQuery);
        Object[] getSearchBrandParams = new Object[]{userIdx,'%'+searchWord+'%','%'+searchWord+'%'};
        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new GetBrandListRes(
                        rs.getInt("Brands.id"),
                        rs.getString("name"),
                        rs.getString("englishName"),
                        rs.getInt("productNum"),
                        rs.getBoolean("isExist"),
                        rs.getString("imageUrl")
                ),getSearchBrandParams);
    }

    public List<getFollowBrandRes> getFollowedBrandList(int userIdx) {
        String getBrandQuery = "select Brands.id,name,englishName,imageUrl,userId,exists(select * from BrandFollows where userId = ? and brandId = Brands.id)as isExist,productNum\n" +
                "from Brands left outer join BrandFollows on Brands.id = BrandFollows.brandId \n" +
                "left outer join (select tag,count(*)as productNum\n" +
                "from ProductTags group by tag)b on name = tag or englishName = tag\n" +
                "group by name having isExist = 1 order by BrandFollows.createdAt desc" ;

        String GetProductQuery = "SELECT Products.productId,name,price,region,isSafePayment," +
                "case" +
                "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전')" +
                "    when (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전')" +
                "    when (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전')" +
                "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전')" +
                " END AS elapsedTime, imageUrl,likeCount" +
                " FROM (Products left outer join (SELECT imageUrl, productId" +
                " FROM (SELECT *" +
                "       FROM ProductImages" +
                "       ORDER BY createdAt)b" +
                " GROUP BY productId" +
                " )as imageTable" +
                " on imageTable.productId = Products.productId)" +
                " left outer join (select productId,count(*) as likeCount from Likes group by productId)as c" +
                " on Products.productId =  c.productId" +
                " where Products.productId in (select productId from ProductTags where tag = ? or ?) and  Products.status='SALE' limit 10";


        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new getFollowBrandRes(
                        rs.getInt("Brands.id"),
                        rs.getString("name"),
                        rs.getString("englishName"),
                        rs.getInt("productNum"),
                        rs.getBoolean("isExist"),
                        rs.getString("imageUrl"),
                        this.jdbcTemplate.query(GetProductQuery,
                                (rs2, rowNum2) -> new GetProductRes(
                                        rs2.getInt("Products.productId"),
                                        rs2.getString("imageUrl"),
                                        this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ?) as b",
                                          (rs3, rowNum3) -> new Integer(
                                              rs3.getInt("b")),
                                             userIdx,rs2.getInt("Products.productId")),
                                         rs2.getInt("price"),
                                         rs2.getString("name"),
                                         rs2.getString("region"),
                                         rs2.getString("elapsedTime"),
                                         rs2.getInt("isSafePayment"),
                                         rs2.getInt("likeCount")
                ),new Object[]{rs.getString("name"),rs.getString("englishName")})
                ),userIdx);



    }

    public List<getFollowBrandRes> getRecommendBrandList(int userIdx) {
        String getBrandQuery = "select Brands.id,name,englishName,imageUrl,userId,exists(select * from BrandFollows where userId = ? and brandId = Brands.id)as isExist,productNum\n" +
                "from Brands left outer join BrandFollows on Brands.id = BrandFollows.brandId \n" +
                "left outer join (select tag,count(*)as productNum\n" +
                "from ProductTags group by tag)b on name = tag or englishName = tag\n" +
                "group by name having isExist = 0 order by rand() limit 10";

        String GetProductQuery = "SELECT Products.productId,name,price,region,isSafePayment," +
                "case" +
                "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전')" +
                "    when (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전')" +
                "    when (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전')" +
                "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전')" +
                " END AS elapsedTime, imageUrl,likeCount" +
                " FROM (Products left outer join (SELECT imageUrl, productId" +
                " FROM (SELECT *" +
                "       FROM ProductImages" +
                "       ORDER BY createdAt)b" +
                " GROUP BY productId" +
                " )as imageTable" +
                " on imageTable.productId = Products.productId)" +
                " left outer join (select productId,count(*) as likeCount from Likes group by productId)as c" +
                " on Products.productId =  c.productId" +
                " where Products.productId in (select productId from ProductTags where tag = ? or ?) and Products.status='SALE' limit 10";


        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new getFollowBrandRes(
                        rs.getInt("Brands.id"),
                        rs.getString("name"),
                        rs.getString("englishName"),
                        rs.getInt("productNum"),
                        rs.getBoolean("isExist"),
                        rs.getString("imageUrl"),
                        this.jdbcTemplate.query(GetProductQuery,
                                (rs2, rowNum2) -> new GetProductRes(
                                        rs2.getInt("Products.productId"),
                                        rs2.getString("imageUrl"),
                                        this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ?) as b",
                                                (rs3, rowNum3) -> new Integer(
                                                        rs3.getInt("b")),
                                                userIdx, rs2.getInt("Products.productId")),
                                        rs2.getInt("price"),
                                        rs2.getString("name"),
                                        rs2.getString("region"),
                                        rs2.getString("elapsedTime"),
                                        rs2.getInt("isSafePayment"),
                                        rs2.getInt("likeCount")
                                ), new Object[]{rs.getString("name"), rs.getString("englishName")})
                ), userIdx);


    }

    public List<GetBrandListRes> getSearchRecommendBrandList(int userIdx) {
        String getBrandQuery = "select Brands.id,name,englishName,imageUrl,userId,exists(select * from BrandFollows where userId = ? and brandId = Brands.id)as isExist,productNum\n" +
                "from Brands left outer join BrandFollows on Brands.id = BrandFollows.brandId \n" +
                "left outer join (select tag,count(*)as productNum\n" +
                "from ProductTags group by tag)b on name = tag or englishName = tag\n" +
                "group by name order by rand() limit 5" ;

        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new GetBrandListRes(
                        rs.getInt("Brands.id"),
                        rs.getString("name"),
                        rs.getString("englishName"),
                        rs.getInt("productNum"),
                        rs.getBoolean("isExist"),
                        rs.getString("imageUrl")
                ),userIdx);
    }

    public int isDeletedUser(int userIdx) {
        String isDeletedUserQuery = "select exists(select * from Users where id = ? and status ='DELETED')";
        return this.jdbcTemplate.queryForObject(isDeletedUserQuery, int.class, userIdx);
    }



}