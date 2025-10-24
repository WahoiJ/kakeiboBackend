package com.kakeibo.kakeibo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.entity.MonthlyBudgetsEntity;
import com.kakeibo.kakeibo.service.MonthlyBudgetsService;

@RestController
public class MonthlyBudgetsController {
    @Autowired
    MonthlyBudgetsService monthlyBudgetsService;

    @GetMapping("/api/budgets")
    public List<MonthlyBudgetsEntity> getBudgets(@RequestParam("userId") Integer userId) {
        return monthlyBudgetsService.getBudgetsByUserId(userId);
    }
    @PostMapping("/api/budgets")
    public MonthlyBudgetsEntity addBudget(@RequestBody MonthlyBudgetsEntity budget) {
        return monthlyBudgetsService.saveBudget(budget);
    }
}
