package com.kakeibo.kakeibo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    private Integer userId;

    @JsonProperty("budget_month")
    @Column(name = "budget_month")
    private String budgetMonth;

    /** 使ってよい金額。逆算モードでは 収入 − 固定費 − 貯めたい額 */
    @JsonProperty("available_amount")
    @Column(name = "available_amount")
    private Double availableAmount;

    /** 収入（逆算モードのみ。直接入力の場合は null） */
    @JsonProperty("income")
    @Column(name = "income")
    private Double income;

    /** 固定費の合計（家賃・通信費など）。項目がある場合はその合計 */
    @JsonProperty("fixed_costs")
    @Column(name = "fixed_costs")
    private Double fixedCosts;

    /** 固定費の内訳（null なら合計のみ入力） */
    @JsonProperty("fixed_cost_items")
    @Convert(converter = FixedCostItemsConverter.class)
    @Column(name = "fixed_cost_items", columnDefinition = "text")
    private List<FixedCostItem> fixedCostItems;

    /** 貯めたい額 */
    @JsonProperty("savings_goal")
    @Column(name = "savings_goal")
    private Double savingsGoal;
}