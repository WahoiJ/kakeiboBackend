// filepath: d:\Programming\Kakeibo\backend\kakeibo\src\main\java\com\kakeibo\kakeibo\entity\MonthlyBudgetsEntity.java
package com.kakeibo.kakeibo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "monthly_budgets", schema = "public")
@Data
public class MonthlyBudgetsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Integer budgetId;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Integer userId;  // 変更: user_id -> userId

    @JsonProperty("budget_month")
    @Column(name = "budget_month")
    private String budgetMonth;  // 変更: budget_month -> budgetMonth

    @JsonProperty("available_amount")
    @Column(name = "available_amount")
    private Double availableAmount;
}