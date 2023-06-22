package com.example.demo.database.endpointconfig;

import com.example.demo.util.HttpMethodConverter;
import com.example.demo.util.StringListConverter;
import jakarta.persistence.*;
import org.springframework.http.HttpMethod;

import java.util.List;

@Entity
@Table(name = "endpoint_configurations")
public class EndpointConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String endpointId;

    @Column
    private String path;



    @Convert(converter = StringListConverter.class)
    @Column(name = "consumes", columnDefinition = "TEXT")
    private List<String> consumes;

    @Convert(converter = StringListConverter.class)
    @Column(name = "produces", columnDefinition = "TEXT")
    private List<String> produces;

    @Convert(converter = HttpMethodConverter.class)
    @Column(name = "http_method")
    private HttpMethod httpMethod;

    // Other fields

    @Column(columnDefinition = "TEXT")
    private String expectedRequestBody;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getExpectedRequestBody() {
        return expectedRequestBody;
    }

    public void setExpectedRequestBody(String expectedRequestBody) {
        this.expectedRequestBody = expectedRequestBody;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    @Override
    public String toString() {
        return "EndpointConfigurationEntity{" +
                "id=" + id +
                ", endpointId='" + endpointId + '\'' +
                ", path='" + path + '\'' +
                ", consumes=" + consumes +
                ", produces=" + produces +
                ", httpMethod=" + httpMethod +
                ", expectedRequestBody='" + expectedRequestBody + '\'' +
                '}';
    }
}
