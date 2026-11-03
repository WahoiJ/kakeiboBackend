package com.kakeibo.kakeibo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.config.AuthUser;
import com.kakeibo.kakeibo.entity.MonthlyExpensesEntity;
import com.kakeibo.kakeibo.service.MonthlyExpensesService;

/** 月別出費は日々の出費から自動計算されるため、参照のみ提供する */
@RestController
public class MonthlyExpensesController {
    @Autowired
    private MonthlyExpensesService monthlyExpensesService;

    @GetMapping("/api/monthly-expenses")
    public List<MonthlyExpensesEntity> getExpenses(@RequestParam(value = "userId", required = false) Integer userId) {
        return monthlyExpensesService.getExpensesByUserId(AuthUser.requireSelf(userId));
    }

    @GetMapping("/api/monthly-expenses/current")
    public MonthlyExpensesEntity getCurrentMonthExpense(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam("budgetMonth") String budgetMonth) {
        return monthlyExpensesService.getExpensesByUserIdAndMonth(AuthUser.requireSelf(userId), budgetMonth).orElse(null);
    }
}
