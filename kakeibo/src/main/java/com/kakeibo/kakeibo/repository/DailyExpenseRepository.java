package com.kakeibo.kakeibo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakeibo.kakeibo.entity.DailyExpenseEntity;

public interface DailyExpenseRepository extends JpaRepository<DailyExpenseEntity, Integer>{

    public List<DailyExpenseEntity> findByUserId(Long userId);

}
