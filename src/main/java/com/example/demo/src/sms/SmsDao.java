package com.example.demo.src.sms;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SmsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkExistingUser(String phoneNum) {
        return this.jdbcTemplate.queryForObject("select count(*) from SMSAuths where phoneNum = ?",
                (rs, rowNum) -> rs.getInt("count(*)"), phoneNum);

    }


    public int updateAuth(String phoneNum, String numStr) {

        String createAuthQuery = "update SMSAuths set authCode = ? where phoneNum = ?";
        Object[] createAuthParams = new Object[]{numStr, phoneNum};
        return this.jdbcTemplate.update(createAuthQuery, createAuthParams);

    }

    public int createAuth(String phoneNum, String numStr) {

        String createAuthQuery = "insert into SMSAuths (phoneNum, authCode) VALUES (?,?)";
        Object[] createAuthParams = new Object[]{phoneNum, numStr};
        return this.jdbcTemplate.update(createAuthQuery, createAuthParams);
    }
}