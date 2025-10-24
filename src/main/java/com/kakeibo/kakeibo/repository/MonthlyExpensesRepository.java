package com.kakeibo.kakeibo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakeibo.kakeibo.entity.MonthlyExpensesEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyExpensesRepository extends JpaRepository<MonthlyExpensesEntity, Integer> {
    List<MonthlyExpensesEntity> findByUserId(Integer userId);
    Optional<MonthlyExpensesEntity> findByUserIdAndBudgetMonth(Integer userId, String budgetMonth);
}
