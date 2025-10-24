package com.kakeibo.kakeibo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakeibo.kakeibo.entity.MonthlyBudgetsEntity;
import com.kakeibo.kakeibo.repository.MonthlyBudgetsRepository;

@Service
public class MonthlyBudgetsService {
    @Autowired
    private MonthlyBudgetsRepository repository;

    public List<MonthlyBudgetsEntity> getBudget(){
        return repository.findAll();
    }

    public List<MonthlyBudgetsEntity> getBudgetsByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    public MonthlyBudgetsEntity saveBudget(MonthlyBudgetsEntity budget){
        Optional<MonthlyBudgetsEntity> existing = repository.findByUserIdAndBudgetMonth(budget.getUserId(), budget.getBudgetMonth());
        
        if(existing.isPresent()){
            MonthlyBudgetsEntity entity = existing.get();//Optionalはラッパーなので直接値にアクセスできない
            entity.setAvailableAmount(budget.getAvailableAmount());
            return repository.save(entity);
        }else{
            return repository.save(budget);
        }
    }
}
