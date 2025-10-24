package com.kakeibo.kakeibo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "monthly_expenses", schema = "public")
@Data
public class MonthlyExpensesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "total_expense_id")
    private Integer totalExpenseId;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Integer userId;

    @JsonProperty("budget_month")
    @Column(name = "budget_month")
    private String budgetMonth;

    @JsonProperty("amount")
    @Column(name = "amount")
    private Double amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
