package com.example.demo.src.address;

import com.example.demo.src.address.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createAddress(Address address) {

        String createAddressQuery = "insert into Address(userId, nickName, address, detailAddress, latitude, longitude, phoneNum, isPrimaryAddress)\n" +
                "values(?,?,?,?,?,?,?,?) ";
        Object[] createAddressParams = new Object[]{address.getUserId(), address.getNickName(), address.getAddress(), address.getDetailAddress(),
                address.getLatitude(), address.getLongitude(), address.getPhoneNum(), address.getIsPrimaryAddress()};

        return this.jdbcTemplate.update(createAddressQuery, createAddressParams);

    }

    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where id = ? and status = 'DELETED')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }
}
