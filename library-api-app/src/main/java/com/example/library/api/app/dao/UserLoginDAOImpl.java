package com.example.library.api.app.dao;

import com.example.library.api.app.bean.UserLogin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Optional;

@Component
public class UserLoginDAOImpl implements UserLoginDAO {

    private static final String SQL_GET_USER_FOR_USERNAME =
            "SELECT username,password,roles,email FROM user_login WHERE username = ?";
    private static final String SQL_CREATE_NEW_USER =
            "INSERT INTO user_login (username,password,roles,email) VALUES(?,?,?,?)";

    private final JdbcTemplate jdbcTemplate;

    public UserLoginDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserLogin> getUserForUsername(String username) {
        ResultHolder<UserLogin> resultHolder = new ResultHolder<>();
        jdbcTemplate.query(SQL_GET_USER_FOR_USERNAME,new Object[]{username},
                new int[]{Types.VARCHAR},resultSet -> {
                    UserLogin userLogin = new UserLogin();
                    userLogin.setUsername(resultSet.getString("username"));
                    userLogin.setPassword(resultSet.getString("password"));
                    userLogin.setRole(resultSet.getString("roles"));
                    userLogin.setEmail(resultSet.getString("email"));
                    resultHolder.setResult(userLogin);
                });
        return resultHolder.getResultOptional();
    }

    @Override
    public void createNewUser(UserLogin userLogin) {
        jdbcTemplate.update(SQL_CREATE_NEW_USER,
                userLogin.getUsername(),
                userLogin.getPassword(),
                userLogin.getRole(),
                userLogin.getEmail());
    }
}
