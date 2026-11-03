package com.kakeibo.kakeibo.entity;

/** 固定費の1項目（例: 家賃 80,000円） */
public record FixedCostItem(String name, Double amount) {
}
