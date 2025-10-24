package com.kakeibo.kakeibo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakeibo.kakeibo.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    Optional<UserEntity> findByUserName(String userName);
}
