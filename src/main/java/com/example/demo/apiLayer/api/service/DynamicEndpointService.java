package com.example.demo.apiLayer.api.service;

import com.example.demo.apiLayer.api.dynamicController;
import com.example.demo.database.endpointconfig.EndpointConfigurationEntity;
import com.example.demo.database.endpointconfig.EndpointConfigurationRepository;
import com.example.demo.apiLayer.api.object.dynamicEndpointConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DynamicEndpointService {

    @Autowired
    private EndpointRegistry endpointRegistry;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    Logger log = LoggerFactory.getLogger(DynamicEndpointService.class);

    // Inject the DynamicController bean
    @Autowired
    private dynamicController dynamicController;

    @Autowired
    private EndpointConfigurationRepository endpointConfigurationRepository;



    public void registerEndpointsFromDatabase() {
        endpointConfigurationRepository.deleteAll();
        log.info("entering registerEndpointsFromDatabase");
        List<EndpointConfigurationEntity> endpointConfigs = endpointConfigurationRepository.findAll();
        for (EndpointConfigurationEntity entity : endpointConfigs) {

            dynamicEndpointConfig endpointConfig = convertEntityToConfig(entity);
            log.info("endpointConfig -> {}" , endpointConfig);
            if (endpointConfig != null) {
                registerEndpoint(endpointConfig);
            }
        }
    }

    private dynamicEndpointConfig convertEntityToConfig(EndpointConfigurationEntity entity) {
        log.info("entering convertEntityToConfig");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> expectedRequestBody = objectMapper.readValue(entity.getExpectedRequestBody(), new TypeReference<Map<String, Object>>() {});

            dynamicEndpointConfig config = new dynamicEndpointConfig();
            config.setId(entity.getEndpointId());
            config.setPath(entity.getPath());
            config.setHttpMethod(entity.getHttpMethod());
            config.setConsumes(entity.getConsumes());
            config.setProduces(new ArrayList<>());
            // ... Set other fields
            config.setExpectedRequestBody(expectedRequestBody);
            log.info("config -> {}" , config);
            return config;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void saveEndpointConfiguration(dynamicEndpointConfig endpointConfig) {
        log.info("entering saveEndpointConfiguration");
        EndpointConfigurationEntity entity = new EndpointConfigurationEntity();
        entity.setEndpointId(endpointConfig.getId());
        entity.setPath(endpointConfig.getPath());
        entity.setHttpMethod(endpointConfig.getHttpMethod());
        entity.setConsumes(endpointConfig.getConsumes());
        entity.setProduces(endpointConfig.getProduces());
       // entity.setId(endpointConfig.getId());
        //entity.setExpectedRequestBody(endpointConfig.getExpectedRequestBody());
        // Other fields

        // Serializing expectedRequestBody as JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String expectedRequestBodyJson = objectMapper.writeValueAsString(endpointConfig.getExpectedRequestBody());
            entity.setExpectedRequestBody(expectedRequestBodyJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("entity -> {}" , entity);
        log.info("entity findAll-> {}" ,  endpointConfigurationRepository.findAll());

        endpointConfigurationRepository.save(entity);
        log.info("entity findAll-> {}" ,  endpointConfigurationRepository.findAll());
    }

    public void createEndpoint(dynamicEndpointConfig endpointConfig){
        log.info("entering saveEndpointConfiguration");
        log.info("entity -> {}" , endpointConfig);
        // Define the information needed for the endpoint
        // Map this info to your generic controller
        /*
        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
options.setPatternParser(new PathPatternParser());

String key = "/v5/person/{id}"
RequestMethod requestMethod = RequestMethod.GET
RequestMappingInfo requestMappingInfo =
                    RequestMappingInfo.paths(key)
                            .methods(requestMethod)
                            .options(options)  //This is Builder config
                            .build();
         */

        // You may have to create an instance of `RequestMappingInfo` and `HandlerMethod`
        // and then call 'requestMappingHandlerMapping.registerMapping()' method.
        // Define the information needed for the endpoint
        Map<String, String> patterns = Collections.singletonMap("patterns", endpointConfig.getPath());
        PatternsRequestCondition urlPatterns = new PatternsRequestCondition(endpointConfig.getPath());
        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(new PathPatternParser());

        RequestMethodsRequestCondition httpMethods = new RequestMethodsRequestCondition(RequestMethod.valueOf(endpointConfig.getHttpMethod().name()));

        // Create an instance of RequestMappingInfo
        // Use RequestMappingInfo.Builder
        RequestMappingInfo.Builder requestMappingInfo =
                RequestMappingInfo.paths(endpointConfig.getPath())
                        .methods(RequestMethod.valueOf(endpointConfig.getHttpMethod().name()))
                        .options(options)  //This is Builder config
                        .consumes(endpointConfig.getConsumes().toArray(new String[0]))
                        .produces(endpointConfig.getProduces().toArray(new String[0]));


        // Create an instance of RequestMappingInfo
        RequestMappingInfo mappingInfo = requestMappingInfo.build();
        log.info("mappingInfo -> {} ", mappingInfo);

        // Register the endpoint mapping
        try {
            requestMappingHandlerMapping.registerMapping(mappingInfo, dynamicController, dynamicControllerMethod());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Store the EndpointConfig in the registry
        endpointRegistry.registerEndpoint(endpointConfig.getPath(), endpointConfig);
    }
    public void registerEndpoint(dynamicEndpointConfig endpointConfig) {
        log.info("Entering registerEndpoint");
        //create and save locally
        createEndpoint(endpointConfig);



        log.info("leaving registerEndpoint");
    }
    public void saveEndpoint(dynamicEndpointConfig endpointConfig) {
        //save forever
        saveEndpointConfiguration(endpointConfig);
    }

    private Method dynamicControllerMethod() throws NoSuchMethodException {
        return dynamicController.class.getMethod("handleDynamicEndpoint", HttpServletRequest.class, HttpServletResponse.class);
    }
}