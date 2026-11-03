package com.kakeibo.kakeibo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakeibo.kakeibo.entity.DailyExpenseEntity;
import com.kakeibo.kakeibo.entity.MonthlyExpensesEntity;
import com.kakeibo.kakeibo.repository.DailyExpenseRepository;
import com.kakeibo.kakeibo.repository.MonthlyExpensesRepository;
import com.kakeibo.kakeibo.util.MonthUtil;

/**
 * 月別出費（monthly_expenses）は日々の出費（daily_expenses）の月ごとの合計。
 * フロントエンドからは直接書き込まず、常に日々の出費から計算して保存する。
 */
@Service
public class MonthlyExpensesService {
    @Autowired
    private MonthlyExpensesRepository monthlyExpensesRepository;

    @Autowired
    private DailyExpenseRepository dailyExpenseRepository;

    /** 全月分を日々の出費から再計算して返す（既存データのずれも自動で修正される） */
    @Transactional
    public List<MonthlyExpensesEntity> getExpensesByUserId(Integer userId) {
        Map<String, Double> totals = dailyExpenseRepository.findByUserIdOrderByExpenseDateDescExpenseIdDesc(userId).stream()
                .filter(e -> e.getExpenseDate() != null && e.getExpenseDate().length() >= 7)
                .collect(Collectors.groupingBy(e -> e.getExpenseDate().substring(0, 7),
                        Collectors.summingDouble(e -> Objects.requireNonNullElse(e.getAmount(), 0.0))));

        Set<String> months = new TreeSet<>(totals.keySet());
        monthlyExpensesRepository.findByUserId(userId).forEach(m -> months.add(m.getBudgetMonth()));

        return months.stream()
                .map(month -> upsert(userId, month, totals.getOrDefault(month, 0.0)))
                .toList();
    }

    @Transactional
    public Optional<MonthlyExpensesEntity> getExpensesByUserIdAndMonth(Integer userId, String budgetMonth) {
        return Optional.of(recalculate(userId, MonthUtil.normalizeMonth(budgetMonth)));
    }

    /** 指定月（"YYYY-MM"）の合計を日々の出費から再計算して保存する */
    @Transactional
    public MonthlyExpensesEntity recalculate(Integer userId, String budgetMonth) {
        double total = dailyExpenseRepository.findByUserIdAndExpenseDateStartingWith(userId, budgetMonth + "-").stream()
                .mapToDouble(DailyExpenseEntity::getAmount)
                .sum();
        return upsert(userId, budgetMonth, total);
    }

    private MonthlyExpensesEntity upsert(Integer userId, String budgetMonth, double amount) {
        LocalDateTime now = LocalDateTime.now();
        MonthlyExpensesEntity entity = monthlyExpensesRepository.findByUserIdAndBudgetMonth(userId, budgetMonth)
                .orElseGet(() -> {
                    MonthlyExpensesEntity e = new MonthlyExpensesEntity();
                    e.setUserId(userId);
                    e.setBudgetMonth(budgetMonth);
                    e.setCreatedAt(now);
                    return e;
                });
        if (entity.getTotalExpenseId() == null || !Objects.equals(entity.getAmount(), amount)) {
            entity.setAmount(amount);
            entity.setUpdatedAt(now);
            entity = monthlyExpensesRepository.save(entity);
        }
        return entity;
    }
}
