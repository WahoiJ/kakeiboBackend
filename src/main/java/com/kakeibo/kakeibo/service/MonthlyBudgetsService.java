package com.kakeibo.kakeibo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kakeibo.kakeibo.entity.MonthlyBudgetsEntity;
import com.kakeibo.kakeibo.repository.MonthlyBudgetsRepository;
import com.kakeibo.kakeibo.util.MonthUtil;

@Service
public class MonthlyBudgetsService {
    @Autowired
    private MonthlyBudgetsRepository repository;

    public List<MonthlyBudgetsEntity> getBudgetsByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    /**
     * 同じユーザー・同じ月の予算があれば更新、なければ作成。
     * income が指定されていれば 収入 − 固定費 − 貯めたい額 で使える金額を逆算する（サーバー側で計算）。
     * income が null なら available_amount をそのまま使う（直接入力）。
     */
    @Transactional
    public MonthlyBudgetsEntity saveBudget(Integer userId, MonthlyBudgetsEntity budget) {
        Double income = budget.getIncome();
        Double fixedCosts = null;
        Double savingsGoal = null;
        double available;
        if (income != null) {
            fixedCosts = nonNegative(budget.getFixedCosts(), "固定費");
            savingsGoal = nonNegative(budget.getSavingsGoal(), "貯めたい額");
            if (income < 0) {
                throw badRequest("収入は0円以上を指定してください");
            }
            available = income - fixedCosts - savingsGoal;
            if (available < 0) {
                throw badRequest("固定費と貯めたい額の合計が収入を超えています");
            }
        } else {
            if (budget.getAvailableAmount() == null || budget.getAvailableAmount() < 0) {
                throw badRequest("使ってよい金額は0円以上を指定してください");
            }
            available = budget.getAvailableAmount();
        }

        String month = MonthUtil.normalizeMonth(budget.getBudgetMonth());
        MonthlyBudgetsEntity entity = repository.findByUserIdAndBudgetMonth(userId, month)
                .orElseGet(() -> {
                    MonthlyBudgetsEntity e = new MonthlyBudgetsEntity();
                    e.setUserId(userId);
                    e.setBudgetMonth(month);
                    return e;
                });
        entity.setAvailableAmount(available);
        entity.setIncome(income);
        entity.setFixedCosts(fixedCosts);
        entity.setSavingsGoal(savingsGoal);
        return repository.save(entity);
    }

    private static double nonNegative(Double value, String label) {
        if (value == null) {
            return 0.0;
        }
        if (value < 0) {
            throw badRequest(label + "は0円以上を指定してください");
        }
        return value;
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
