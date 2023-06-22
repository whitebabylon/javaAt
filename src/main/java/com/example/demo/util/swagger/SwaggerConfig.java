package com.example.demo.util.swagger;

import com.example.demo.database.endpointconfig.EndpointConfigurationEntity;
import com.example.demo.database.endpointconfig.EndpointConfigurationRepository;
import com.example.demo.apiLayer.api.object.dynamicEndpointConfig;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SwaggerConfig {
    Logger log = LoggerFactory.getLogger(SwaggerConfig.class);
    @Autowired
    private EndpointConfigurationRepository endpointConfigurationRepository;

    List<EndpointConfigurationEntity> endpointConfigs;



    @PostConstruct
    public void init() {
        // Access the repository here
         endpointConfigs = endpointConfigurationRepository.findAll();
        log.info("endpointConfigs -> {}" , endpointConfigs);
        // Now do the necessary configurations using the entities.

    }
/*

    @Bean
    public Docket api(DynamicEndpointService dynamicEndpointService) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();

        // Add dynamic endpoints

        List<EndpointConfigurationEntity> endpointConfigs = endpointConfigurationRepository.findAll();
        for (EndpointConfigurationEntity entity : endpointConfigs) {

            dynamicEndpointConfig endpointConfig = convertEntityToConfig(entity);

            if (endpointConfig != null) {
                // Programmatically add dynamic endpoints to Swagger

            }
        }

        return docket;
    }
*/
/*
    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {

        @Bean
        public Docket apiDocket() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("your.base.package"))
                    .paths(PathSelectors.any())
                    .build();
        }
    }*/
    /*
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Custom API")
                        .version("1.0")
                        .description("This is a custom API"));
    }*/
/*
    @Bean
    public OpenAPI customOpenAPI() {
        log.info("customOPEJNAPI");
        OpenAPI openAPI = new OpenAPI();


        for (EndpointConfigurationEntity entity : endpointConfigs) {
            log.info("customOpenAPI entity -> {}" , entity);
            dynamicEndpointConfig endpointConfig = convertEntityToConfig(entity);

                String path = endpointConfig.getPath();
                HttpMethod httpMethod = endpointConfig.getHttpMethod();
                List<String> consumes = endpointConfig.getConsumes();
                List<String> produces = endpointConfig.getProduces();
                Map<String, Object> expectedRequestBody = endpointConfig.getExpectedRequestBody();

                // Create OpenAPI path
                PathItem pathItem = new PathItem();
                io.swagger.v3.oas.models.Operation operation = new io.swagger.v3.oas.models.Operation();
                operation.setSummary("Dynamically created endpoint");

                if (expectedRequestBody != null && !expectedRequestBody.isEmpty()) {
                    RequestBody requestBody = new RequestBody();
                    Content content = new Content();
                    Schema schema = new Schema<>();
                    for (String key : expectedRequestBody.keySet()) {
                        schema.addProperties(key, new StringSchema());
                    }
                    MediaType mediaType = new MediaType();
                    mediaType.setSchema(schema);
                    for (String consume : consumes) {
                        content.addMediaType(consume, mediaType);
                    }
                    requestBody.setContent(content);
                    operation.setRequestBody(requestBody);
                }


                // Set response content type
                ApiResponse apiResponse = new ApiResponse().description("Success");
                for (String produce : produces) {
                    apiResponse.content(new Content().addMediaType(produce, new MediaType()));
                }
                ApiResponses apiResponses = new ApiResponses();
                apiResponses.addApiResponse("200", apiResponse);
                operation.setResponses(apiResponses);

                // Assign operation to the correct HTTP method
                if (GET.equals(httpMethod)) {
                    pathItem.setGet(operation);
                } else if (POST.equals(httpMethod)) {
                    pathItem.setPost(operation);
                } else if (PUT.equals(httpMethod)) {
                    pathItem.setPut(operation);
                } else if (DELETE.equals(httpMethod)) {
                    pathItem.setDelete(operation);
                    // ... Handle other HTTP methods
                }

                // Add the constructed pathItem to OpenAPI documentation
                openAPI.path(path, pathItem);

        }
        return openAPI;
    }
    */

    private dynamicEndpointConfig convertEntityToConfig(EndpointConfigurationEntity entity) {

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

            return config;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
