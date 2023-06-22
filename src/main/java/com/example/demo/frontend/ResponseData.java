package com.example.demo.frontend;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseData {
    private List<String> responseTimes;
    private List<String> responseMessages;
    private List<LocalDateTime> data;

    public ResponseData() {
        responseTimes = new ArrayList<>();
        responseMessages = new ArrayList<>();
        data = new ArrayList<>();
    }

    public List<String> getResponseTimes() {
        return responseTimes;
    }

    public List<String> getResponseMessages() {
        return responseMessages;
    }

    public List<LocalDateTime> getData() {
        return data;
    }
}
