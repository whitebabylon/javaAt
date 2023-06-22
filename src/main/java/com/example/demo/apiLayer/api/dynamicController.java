package com.example.demo.apiLayer.api;


import com.example.demo.apiLayer.api.object.ValidationResult;
import com.example.demo.apiLayer.api.object.dynamicEndpointConfig;
import com.example.demo.apiLayer.api.service.EndpointRegistry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class dynamicController {
    Logger log = LoggerFactory.getLogger(dynamicController.class);


    @Autowired
    private EndpointRegistry endpointRegistry;

    @RequestMapping(value = "/dynamic/{endpointId}")
    public ResponseEntity<Object> handleDynamicEndpoint2(
            @PathVariable String endpointId,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("Request -> {} ", request);
        log.info("response -> {} ", response);
        log.info("endpointId -> {} ", endpointId);

        return null;

        // Use the endpointId to fetch the configuration from storage
        // Process the request accordingly and return the response
    }
/*
    public ResponseEntity<Object> handleDynamicEndpoint(HttpServletRequest request, HttpServletResponse response) {
        // Handle the request here...
        return ResponseEntity.ok("Dynamically registered endpoint");
    }
*/

public ResponseEntity<Object> handleDynamicEndpoint(HttpServletRequest request, HttpServletResponse response) {
    log.info("entering handleDynamicEndpoint");
    String requestURI = request.getRequestURI();
    dynamicEndpointConfig endpointConfig = endpointRegistry.getEndpointConfig(requestURI);

    if (endpointConfig == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint not found");
    }

    String contentType = request.getContentType();
    Map<String, Object> payloadAsMap = null;

    try {
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            payloadAsMap = parseJsonPayloadToMap(request);
        } else if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
            payloadAsMap = parseXmlPayloadToMap(request);
        } else {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported Content-Type");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing payload: " + e.getMessage());
    }

    // Validate the request payload
    ValidationResult validationResult = validatePayload(payloadAsMap, endpointConfig.getExpectedRequestBody());
    if (!validationResult.isValid()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getErrorMessage());
    }


    log.info("payload -> {}", payloadAsMap);
    return ResponseEntity.ok(payloadAsMap);
}

    private Map<String, Object> parseJsonPayloadToMap(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {});
    }

    private Map<String, Object> parseXmlPayloadToMap(HttpServletRequest request) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {});

    }

    private Object processIncomingRequest(Map<String, Object> payload) {
        // Your custom processing logic here
        // ...
        log.info("payload -> {}", payload);
        return new Object(); // Placeholder
    }


    private ValidationResult validatePayload(Map<String, Object> payload, Map<String, Object> expectedRequestBody) {
        List<String> missingKeys = new ArrayList<>();
        List<String> extraKeys = new ArrayList<>();

        // Recursive function to check the payload
        checkKeys(payload, expectedRequestBody, missingKeys, extraKeys, "");

        if (missingKeys.isEmpty() && extraKeys.isEmpty()) {
            return ValidationResult.valid();
        } else {
            String errorMessage = "";
            if (!missingKeys.isEmpty()) {
                errorMessage += "Missing keys: " + String.join(", ", missingKeys) + ". ";
            }
            if (!extraKeys.isEmpty()) {
                errorMessage += "Extra keys: " + String.join(", ", extraKeys) + ".";
            }
            return ValidationResult.invalid(errorMessage);
        }
    }
    private void checkKeys(Map<String, Object> payload, Map<String, Object> expected, List<String> missingKeys, List<String> extraKeys, String parentKey) {
        for (String key : expected.keySet()) {
            String fullKey = parentKey.isEmpty() ? key : parentKey + "." + key;
            if (!payload.containsKey(key)) {
                missingKeys.add(fullKey);
            } else if (expected.get(key) instanceof Map && payload.get(key) instanceof Map) {
                // Recursively check nested maps
                checkKeys((Map<String, Object>) payload.get(key), (Map<String, Object>) expected.get(key), missingKeys, extraKeys, fullKey);
            }
        }
        // Check for extra keys
        for (String key : payload.keySet()) {
            String fullKey = parentKey.isEmpty() ? key : parentKey + "." + key;
            if (!expected.containsKey(key)) {
                extraKeys.add(fullKey);
            }
        }
    }

    // Process the payload map (this is where your custom logic goes)
    /*
    Object processingResult = processIncomingRequest(payloadAsMap);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    String json = "";
    try {
        json = ow.writeValueAsString(processingResult);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }

     */

}