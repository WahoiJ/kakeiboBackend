package com.kakeibo.kakeibo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kakeibo.kakeibo.entity.FixedCostItem;
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
        List<FixedCostItem> fixedCostItems = null;
        Double savingsGoal = null;
        double available;
        if (income != null) {
            if (budget.getFixedCostItems() != null) {
                // 項目が送られてきたら合計はサーバー側で計算する
                fixedCostItems = normalizeItems(budget.getFixedCostItems());
                fixedCosts = fixedCostItems.stream().mapToDouble(FixedCostItem::amount).sum();
            } else {
                fixedCosts = nonNegative(budget.getFixedCosts(), "固定費");
            }
            savingsGoal = nonNegative(budget.getSavingsGoal(), "貯めたい額");
            if (income < 0) {
                throw badRequest("収入は0円以上を指定してください");
            }
            // 固定費と貯めたい額が収入を超える場合もそのまま保存する（使えるお金はマイナスになる）
            available = income - fixedCosts - savingsGoal;
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
        entity.setFixedCostItems(fixedCostItems);
        entity.setSavingsGoal(savingsGoal);
        return repository.save(entity);
    }

    /** 空行を除き、名前と金額を検証する */
    private static List<FixedCostItem> normalizeItems(List<FixedCostItem> items) {
        if (items.size() > 50) {
            throw badRequest("固定費の項目は50個までです");
        }
        List<FixedCostItem> result = new ArrayList<>();
        for (FixedCostItem item : items) {
            if (item == null) {
                continue;
            }
            String name = item.name() == null ? "" : item.name().trim();
            if (name.isEmpty() && (item.amount() == null || item.amount() == 0)) {
                continue;
            }
            if (name.length() > 50) {
                throw badRequest("固定費の項目名は50文字以内にしてください");
            }
            double amount = nonNegative(item.amount(), name.isEmpty() ? "固定費" : name);
            result.add(new FixedCostItem(name.isEmpty() ? "その他" : name, amount));
        }
        return result;
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
