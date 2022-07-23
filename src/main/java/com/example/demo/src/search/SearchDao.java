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
        if(type.equals("recent")) {
            getSearchWordQuery = "SELECT id,searchWord FROM bungae.Searches where userIdx = ? and status = 'NORMAL' order by createdAt desc" ;
            return this.jdbcTemplate.query(getSearchWordQuery,
                    (rs, rowNum) -> new GetSearchWordRes(
                            rs.getInt("id"),
                            rs.getString("searchWord")
                    ),userIdx);

        }else{
            getSearchWordQuery = "select id,searchWord,count(*) from Searches group by searchWord order by count(*) desc limit 10";
            return this.jdbcTemplate.query(getSearchWordQuery,
                    (rs, rowNum) -> new GetSearchWordRes(
                            rs.getInt("id"),
                            rs.getString("searchWord")
                    ));
        }
    }

    public int  deleteAllSearchs(int userIdx) throws BaseException {
        String deleteAllSearchsQuery = "update Searches set status = 'DELETED' where userIdx = ?";
        int result = this.jdbcTemplate.update(deleteAllSearchsQuery, userIdx);
        return result;
    }

    public int  deleteSearch(int userIdx,int searchIdx) throws BaseException {
        String deleteAllSearchsQuery = "update Searches set status = 'DELETED' where userIdx = ? and id = ?";
        int result = this.jdbcTemplate.update(deleteAllSearchsQuery, new Object[]{userIdx,searchIdx});
        return result;
    }

    public int isCorrenctUser(int searchIdx)throws BaseException {
        String isCorrenctUserQuery = "select userIdx from Searches where id = ?";
        return this.jdbcTemplate.queryForObject(isCorrenctUserQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("userIdx")
                ),searchIdx);
    }

    public int isExistSearchIdx(int searchIdx)throws BaseException {
        return this.jdbcTemplate.queryForObject( "select exists(select * from Searches where id = ? and status not in ('DELETED'))",int.class,searchIdx );
    }
}