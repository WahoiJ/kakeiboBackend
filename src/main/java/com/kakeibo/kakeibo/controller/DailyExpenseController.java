package com.kakeibo.kakeibo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.entity.DailyExpenseEntity;
import com.kakeibo.kakeibo.service.DailyExpenseService;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
public class DailyExpenseController {
    @Autowired
    private DailyExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<DailyExpenseEntity>> getExpenses(@RequestParam Long userId) {
        List<DailyExpenseEntity> expenses = expenseService.getExpenseByUserId(userId);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<DailyExpenseEntity> createExpense(@RequestBody DailyExpenseEntity expense) {
        DailyExpenseEntity savedExpense = expenseService.saveExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }
}
