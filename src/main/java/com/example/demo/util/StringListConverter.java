package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        try {
            return objectMapper.writeValueAsString(stringList);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}
