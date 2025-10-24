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
    @Column(name = "expense_id")
    private Integer expense_id;
    @JsonProperty("user_id")//後で調べる
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "expense_date")
    private String expense_date;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "date")
    private String date;
}
