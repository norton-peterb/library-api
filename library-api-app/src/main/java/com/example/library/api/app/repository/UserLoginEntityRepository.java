package com.example.library.api.app.repository;

import com.example.library.api.app.entity.UserLoginEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserLoginEntityRepository extends CrudRepository<UserLoginEntity,Long> {
    List<UserLoginEntity> findByUsername(String username);
}
