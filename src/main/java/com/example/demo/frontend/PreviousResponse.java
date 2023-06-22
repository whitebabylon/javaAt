package com.example.demo.frontend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PreviousResponse {
    private List<String> responseTimes;
    private List<String> responseMessages;
    private List<LocalDateTime> data;

    public PreviousResponse(List<String> responseTimes, List<String> responseMessages, List<LocalDateTime> data) {
        this.responseTimes = responseTimes;
        this.responseMessages = responseMessages;
        this.data = data;
    }

    public PreviousResponse() {
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

    @Override
    public String toString() {
        return "PreviousResponse{" +
                "responseTimes=" + responseTimes +
                ", responseMessages=" + responseMessages +
                '}';
    }
}
