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

    /** 同じユーザー・同じ月の予算があれば更新、なければ作成 */
    @Transactional
    public MonthlyBudgetsEntity saveBudget(Integer userId, MonthlyBudgetsEntity budget) {
        if (budget.getAvailableAmount() == null || budget.getAvailableAmount() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "出費可能額は0円以上を指定してください");
        }
        String month = MonthUtil.normalizeMonth(budget.getBudgetMonth());
        MonthlyBudgetsEntity entity = repository.findByUserIdAndBudgetMonth(userId, month)
                .orElseGet(() -> {
                    MonthlyBudgetsEntity e = new MonthlyBudgetsEntity();
                    e.setUserId(userId);
                    e.setBudgetMonth(month);
                    return e;
                });
        entity.setAvailableAmount(budget.getAvailableAmount());
        return repository.save(entity);
    }
}
