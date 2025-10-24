package com.kakeibo.kakeibo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.entity.MonthlyExpensesEntity;
import com.kakeibo.kakeibo.service.MonthlyExpensesService;

@RestController
public class MonthlyExpensesController {
    @Autowired
    private MonthlyExpensesService monthlyExpensesService;

    @GetMapping("/api/monthly-expenses")
    public List<MonthlyExpensesEntity> getExpenses(@RequestParam("userId") Integer userId) {
        return monthlyExpensesService.getExpensesByUserId(userId);
    }

    @GetMapping("/api/monthly-expenses/current")
    public MonthlyExpensesEntity getCurrentMonthExpense(
            @RequestParam("userId") Integer userId,
            @RequestParam("budgetMonth") String budgetMonth) {
        return monthlyExpensesService.getExpensesByUserIdAndMonth(userId, budgetMonth).orElse(null);
    }

    @PostMapping("/api/monthly-expenses")
    public MonthlyExpensesEntity addExpense(@RequestBody MonthlyExpensesEntity expense) {
        return monthlyExpensesService.saveExpense(expense);
    }
}
