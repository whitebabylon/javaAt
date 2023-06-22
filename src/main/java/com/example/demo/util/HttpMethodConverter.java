package com.example.demo.util;

import org.springframework.http.HttpMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class HttpMethodConverter implements AttributeConverter<HttpMethod, String> {

    @Override
    public String convertToDatabaseColumn(HttpMethod httpMethod) {
        return (httpMethod != null) ? httpMethod.name() : null;
    }

    @Override
    public HttpMethod convertToEntityAttribute(String httpMethodName) {
        return (httpMethodName != null) ? HttpMethod.valueOf(httpMethodName) : null;
    }
}
