package com.kakeibo.kakeibo.entity;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** 固定費の項目リストを JSON 文字列として1列に保存する */
@Converter
public class FixedCostItemsConverter implements AttributeConverter<List<FixedCostItem>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<FixedCostItem>> TYPE = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(List<FixedCostItem> items) {
        if (items == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("固定費の項目を保存できません", e);
        }
    }

    @Override
    public List<FixedCostItem> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return MAPPER.readValue(json, TYPE);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
