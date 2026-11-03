package com.kakeibo.kakeibo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.config.AuthUser;
import com.kakeibo.kakeibo.entity.DailyExpenseEntity;
import com.kakeibo.kakeibo.service.DailyExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class DailyExpenseController {
    @Autowired
    private DailyExpenseService expenseService;

    @GetMapping
    public List<DailyExpenseEntity> getExpenses(@RequestParam(value = "userId", required = false) Integer userId) {
        return expenseService.getExpenseByUserId(AuthUser.requireSelf(userId));
    }

    @PostMapping
    public ResponseEntity<DailyExpenseEntity> createExpense(@RequestBody DailyExpenseEntity expense) {
        Integer userId = AuthUser.requireSelf(expense.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.saveExpense(userId, expense));
    }

    @PutMapping("/{expenseId}")
    public DailyExpenseEntity updateExpense(@PathVariable Integer expenseId, @RequestBody DailyExpenseEntity expense) {
        return expenseService.updateExpense(AuthUser.current().id(), expenseId, expense);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer expenseId) {
        expenseService.deleteExpense(AuthUser.current().id(), expenseId);
        return ResponseEntity.noContent().build();
    }
}
