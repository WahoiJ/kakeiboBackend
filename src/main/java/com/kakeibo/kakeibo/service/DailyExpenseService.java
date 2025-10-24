package com.kakeibo.kakeibo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakeibo.kakeibo.entity.DailyExpenseEntity;
import com.kakeibo.kakeibo.repository.DailyExpenseRepository;

@Service
public class DailyExpenseService {
    @Autowired
    private DailyExpenseRepository repository;

    public List<DailyExpenseEntity> getExpenseByUserId(Long userId){
        return repository.findByUserId(userId);
    }

    public DailyExpenseEntity saveExpense(DailyExpenseEntity expense) {
        return repository.save(expense);
    }

    public void deleteExpense(Integer expenseId) {
        repository.deleteById(expenseId);
    }

}
