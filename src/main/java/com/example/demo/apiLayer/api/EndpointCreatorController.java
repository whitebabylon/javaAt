package com.example.demo.apiLayer.api;

import com.example.demo.apiLayer.api.object.dynamicEndpointConfig;
import com.example.demo.apiLayer.api.service.DynamicEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointCreatorController {

    @Autowired
    private DynamicEndpointService dynamicEndpointService;

    @PostMapping("/create-endpoint")
    public ResponseEntity<String> createEndpoint(@RequestBody dynamicEndpointConfig endpointConfig) {
        try {
            dynamicEndpointService.registerEndpoint(endpointConfig);
            dynamicEndpointService.saveEndpoint(endpointConfig);


            return ResponseEntity.ok("Endpoint created successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating endpoint: " + ex.getMessage());
        }
    }
}