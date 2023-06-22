package com.example.demo.apiLayer.api.service;

import com.example.demo.apiLayer.api.object.dynamicEndpointConfig;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EndpointRegistry {

    private Map<String, dynamicEndpointConfig> endpoints = new ConcurrentHashMap<>();

    public void registerEndpoint(String path, dynamicEndpointConfig endpointConfig) {
        endpoints.put(path, endpointConfig);
    }

    public dynamicEndpointConfig getEndpointConfig(String path) {
        return endpoints.get(path);
    }
}