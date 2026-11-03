package com.kakeibo.kakeibo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "daily_expenses", schema = "public")
public class DailyExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("expense_id")
    @Column(name = "expense_id")
    private Integer expenseId;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Integer userId;

    /** YYYY-MM-DD */
    @JsonProperty("expense_date")
    @Column(name = "expense_date")
    private String expenseDate;

    @JsonProperty("amount")
    @Column(name = "amount")
    private Double amount;
}
