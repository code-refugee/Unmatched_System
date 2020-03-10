package com.unmatched.mapper.rowmapper;

import com.unmatched.pojo.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user=new User();
        user.setUserName(resultSet.getString("userName"));
        user.setPassWord(resultSet.getString("passWord"));
        return user;
    }
}
