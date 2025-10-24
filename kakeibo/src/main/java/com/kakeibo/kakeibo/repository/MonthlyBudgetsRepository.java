package com.kakeibo.kakeibo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakeibo.kakeibo.entity.MonthlyBudgetsEntity;

public interface MonthlyBudgetsRepository extends JpaRepository<MonthlyBudgetsEntity, Integer> {

    Optional<MonthlyBudgetsEntity> findByUserIdAndBudgetMonth(Integer userId, String budgetMonth);//user_idとbuget_monthが一致するレコードを検索

    List<MonthlyBudgetsEntity> findByUserId(Integer userId);
}
