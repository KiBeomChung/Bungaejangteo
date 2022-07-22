package com.example.demo.src.brand;

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
}