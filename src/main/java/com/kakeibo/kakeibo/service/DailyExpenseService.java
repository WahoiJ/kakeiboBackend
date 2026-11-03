package com.kakeibo.kakeibo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kakeibo.kakeibo.entity.DailyExpenseEntity;
import com.kakeibo.kakeibo.repository.DailyExpenseRepository;
import com.kakeibo.kakeibo.util.MonthUtil;

@Service
public class DailyExpenseService {
    @Autowired
    private DailyExpenseRepository repository;

    @Autowired
    private MonthlyExpensesService monthlyExpensesService;

    public List<DailyExpenseEntity> getExpenseByUserId(Integer userId) {
        return repository.findByUserIdOrderByExpenseDateDescExpenseIdDesc(userId);
    }

    /** 日々の出費を登録し、その月の月別出費合計を再計算する */
    @Transactional
    public DailyExpenseEntity saveExpense(Integer userId, DailyExpenseEntity expense) {
        if (expense.getAmount() == null || expense.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "出費額は1円以上を指定してください");
        }
        expense.setExpenseId(null);
        expense.setUserId(userId);
        expense.setExpenseDate(MonthUtil.validateDate(expense.getExpenseDate()));
        expense.setCategory(normalizeCategory(expense.getCategory()));
        DailyExpenseEntity saved = repository.save(expense);
        monthlyExpensesService.recalculate(userId, MonthUtil.monthOf(saved.getExpenseDate()));
        return saved;
    }

    /** 本人の出費の金額・日付・カテゴリを更新し、変更前後の月の月別出費合計を再計算する */
    @Transactional
    public DailyExpenseEntity updateExpense(Integer userId, Integer expenseId, DailyExpenseEntity patch) {
        DailyExpenseEntity expense = repository.findById(expenseId)
                .filter(e -> userId.equals(e.getUserId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "出費が見つかりません"));
        if (patch.getAmount() == null || patch.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "出費額は1円以上を指定してください");
        }
        String oldDate = expense.getExpenseDate();
        expense.setAmount(patch.getAmount());
        expense.setExpenseDate(MonthUtil.validateDate(patch.getExpenseDate()));
        expense.setCategory(normalizeCategory(patch.getCategory()));
        DailyExpenseEntity saved = repository.saveAndFlush(expense);

        String newMonth = MonthUtil.monthOf(saved.getExpenseDate());
        monthlyExpensesService.recalculate(userId, newMonth);
        if (oldDate != null && oldDate.length() >= 7 && !oldDate.substring(0, 7).equals(newMonth)) {
            monthlyExpensesService.recalculate(userId, oldDate.substring(0, 7));
        }
        return saved;
    }

    /** 本人の出費のみ削除し、その月の月別出費合計を再計算する */
    @Transactional
    public void deleteExpense(Integer userId, Integer expenseId) {
        DailyExpenseEntity expense = repository.findById(expenseId)
                .filter(e -> userId.equals(e.getUserId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "出費が見つかりません"));
        repository.delete(expense);
        repository.flush();
        if (expense.getExpenseDate() != null) {
            monthlyExpensesService.recalculate(userId, MonthUtil.monthOf(expense.getExpenseDate()));
        }
    }

    /** 前後の空白を除き、空なら null（未分類）。20文字を超える場合は 400 */
    private static String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }
        String trimmed = category.trim();
        if (trimmed.length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "カテゴリは20文字以内にしてください");
        }
        return trimmed;
    }
}
