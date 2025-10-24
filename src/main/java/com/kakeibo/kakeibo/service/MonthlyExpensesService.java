package com.kakeibo.kakeibo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakeibo.kakeibo.entity.MonthlyExpensesEntity;
import com.kakeibo.kakeibo.repository.MonthlyExpensesRepository;

@Service
public class MonthlyExpensesService {
    @Autowired
    private MonthlyExpensesRepository monthlyExpensesRepository;

    public List<MonthlyExpensesEntity> getExpensesByUserId(Integer userId) {
        return monthlyExpensesRepository.findByUserId(userId);
    }

    public Optional<MonthlyExpensesEntity> getExpensesByUserIdAndMonth(Integer userId, String budgetMonth) {
        return monthlyExpensesRepository.findByUserIdAndBudgetMonth(userId, budgetMonth);
    }

    public MonthlyExpensesEntity saveExpense(MonthlyExpensesEntity expense) {
        // 同じ user_id と budget_month のデータが存在するか確認
        Optional<MonthlyExpensesEntity> existingExpense = monthlyExpensesRepository.findByUserIdAndBudgetMonth(
            expense.getUserId(), 
            expense.getBudgetMonth()
        );

        if (existingExpense.isPresent()) {
            // 既存データがあれば UPDATE
            MonthlyExpensesEntity existing = existingExpense.get();
            existing.setAmount(expense.getAmount());
            existing.setUpdatedAt(LocalDateTime.now());
            return monthlyExpensesRepository.save(existing);
        } else {
            // 新規データなら INSERT
            expense.setCreatedAt(LocalDateTime.now());
            expense.setUpdatedAt(LocalDateTime.now());
            return monthlyExpensesRepository.save(expense);
        }
    }

    public void deleteExpense(Integer totalExpenseId) {
        monthlyExpensesRepository.deleteById(totalExpenseId);
    }

}
