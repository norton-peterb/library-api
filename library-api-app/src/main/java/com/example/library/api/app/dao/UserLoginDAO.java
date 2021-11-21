package com.example.library.api.app.dao;

import com.example.library.api.app.bean.UserLogin;

import java.util.Optional;

public interface UserLoginDAO {
    Optional<UserLogin> getUserForUsername(String username);
    void createNewUser(UserLogin userLogin);
}
