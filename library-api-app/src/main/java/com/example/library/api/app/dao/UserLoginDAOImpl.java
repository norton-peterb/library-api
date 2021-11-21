package com.example.library.api.app.dao;

import com.example.library.api.app.bean.UserLogin;
import com.example.library.api.app.entity.UserLoginEntity;
import com.example.library.api.app.repository.UserLoginEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserLoginDAOImpl implements UserLoginDAO {

    private final UserLoginEntityRepository repository;

    public UserLoginDAOImpl(@Autowired UserLoginEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserLogin> getUserForUsername(String username) {
        List<UserLoginEntity> userLoginEntities = repository.findByUsername(username);
        if(userLoginEntities.isEmpty()) {
            return Optional.empty();
        } else if(userLoginEntities.size() == 1) {
            UserLogin userLogin = new UserLogin();
            userLogin.setUsername(userLoginEntities.get(0).getUsername());
            userLogin.setPassword(userLoginEntities.get(0).getPassword());
            userLogin.setRole(userLoginEntities.get(0).getRoles());
            userLogin.setEmail(userLoginEntities.get(0).getEmail());
            return Optional.of(userLogin);
        } else {
            throw new RuntimeException("Unexpected Number of Users found for username");
        }
    }

    @Override
    public void createNewUser(UserLogin userLogin) {
        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setUsername(userLogin.getUsername());
        userLoginEntity.setPassword(userLogin.getPassword());
        userLoginEntity.setRoles(userLogin.getRole());
        userLoginEntity.setEmail(userLogin.getEmail());
        repository.save(userLoginEntity);
    }
}
