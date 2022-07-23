package com.example.demo.src.search;

import com.example.demo.src.brand.model.GetBrandListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
//import com.example.demo.src.search.model.*;
import java.util.List;

@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<String> getSearchWord(int userIdx, String type) {
        String getSearchWordQuery = "";
        if(type.equals("recent")) {
            getSearchWordQuery = "SELECT searchWord FROM bungae.Searches where userIdx = ? and status = 'NORMAL' order by createdAt desc" ;
            return this.jdbcTemplate.query(getSearchWordQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("searchWord")
                    ),userIdx);

        }else{
            getSearchWordQuery = "select searchWord,count(*) from Searches where Searches.status = 'NORMAL' group by searchWord order by count(*) desc limit 10";
            return this.jdbcTemplate.query(getSearchWordQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("searchWord")
                    ));
        }


    }
}