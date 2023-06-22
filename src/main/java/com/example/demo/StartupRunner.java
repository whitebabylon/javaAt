package com.example.demo;

import com.example.demo.apiLayer.api.service.DynamicEndpointService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private DynamicEndpointService dynamicEndpointService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dynamicEndpointService.registerEndpointsFromDatabase();
    }


}
