package com.example.demo.apiLayer.api.object;

import com.example.demo.util.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

public class dynamicEndpointConfig {

    // The unique identifier for this endpoint
    private String id;

    // The path of the endpoint, for example, "/api/users"
    private String path;

    // The HTTP method (GET, POST, PUT, DELETE, etc.)
    private HttpMethod httpMethod;


    private List<String> consumes;

    // The produces clause, example: "application/json"
    private List<String> produces;

    // Custom headers that this endpoint might need
    private Map<String, String> headers;

    // A flag to indicate if this endpoint requires authentication
    private boolean requiresAuthentication;

    // Optional: A role or set of roles that are allowed to access this endpoint
    private List<String> requiredRoles;
/*
    // The expected request body, if applicable. This might be represented in various ways.
    private Object expectedRequestBody;

    // The expected response body, if applicable. This might be represented in various ways.
    private Object expectedResponseBody;

 */

    private Map<String, Object> expectedRequestBody;
    private Map<String, Object> expectedResponseBody;
    // Custom response codes and their respective messages
    private Map<Integer, String> responseCodes;

    // Any additional custom configuration that may be necessary
    private Map<String, Object> additionalConfig;

    // Getters and setters...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean isRequiresAuthentication() {
        return requiresAuthentication;
    }

    public void setRequiresAuthentication(boolean requiresAuthentication) {
        this.requiresAuthentication = requiresAuthentication;
    }

    public List<String> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(List<String> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public Map<String, Object> getExpectedRequestBody() {
        return expectedRequestBody;
    }

    public void setExpectedRequestBody(Map<String, Object> expectedRequestBody) {
        this.expectedRequestBody = expectedRequestBody;
    }

    public Map<String, Object> getExpectedResponseBody() {
        return expectedResponseBody;
    }

    public void setExpectedResponseBody(Map<String, Object> expectedResponseBody) {
        this.expectedResponseBody = expectedResponseBody;
    }

    public Map<Integer, String> getResponseCodes() {
        return responseCodes;
    }

    public void setResponseCodes(Map<Integer, String> responseCodes) {
        this.responseCodes = responseCodes;
    }

    public Map<String, Object> getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(Map<String, Object> additionalConfig) {
        this.additionalConfig = additionalConfig;
    }

    @Override
    public String toString() {
        return "dynamicEndpointConfig{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", httpMethod=" + httpMethod +
                ", consumes=" + consumes +
                ", produces=" + produces +
                ", headers=" + headers +
                ", requiresAuthentication=" + requiresAuthentication +
                ", requiredRoles=" + requiredRoles +
                ", expectedRequestBody=" + expectedRequestBody +
                ", expectedResponseBody=" + expectedResponseBody +
                ", responseCodes=" + responseCodes +
                ", additionalConfig=" + additionalConfig +
                '}';
    }
}