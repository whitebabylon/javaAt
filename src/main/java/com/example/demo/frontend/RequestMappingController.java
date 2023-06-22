package com.example.demo.frontend;

import com.example.demo.apiLayer.api.object.dynamicEndpointConfig;
import com.example.demo.apiLayer.api.service.DynamicEndpointService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class RequestMappingController {
    private static final Logger log = LoggerFactory.getLogger(RequestMappingController.class);

    @Autowired
    private ResponseData responseData;

    @Autowired
    private DynamicEndpointService dynamicEndpointService;

    public List<String> grabAllKnownTypes(){
        List<String> mediaTypes = new ArrayList<>();

        Field[] fields = MediaType.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) &&
                    field.getType().equals(MediaType.class)) {
                try {
                    MediaType mediaType = (MediaType) field.get(null);
                    mediaTypes.add(mediaType.toString());
                } catch (IllegalAccessException e) {
                    // Handle exception if needed
                }
            }
        }
        return mediaTypes;
    }
    @PostMapping("/submit-mapping")
    public String submitMapping(
            @RequestParam("paths") String paths,
            @RequestParam("methods") HttpMethod method,
            @RequestParam("params") String params,
            @RequestParam("headers") String headers,
            @RequestParam("consumes") String consumes,
            @RequestParam("produces") String produces,
            @RequestParam("mappingName") String mappingName,
            @RequestParam("expectedRequestBody") String expectedRequestBody,
            // Other form fields as parameters
            RedirectAttributes redirectAttributes
    ) {

        log.info("Received request with the following parameters:");
        log.info("Paths: {}", paths);
        log.info("Method: {}", method);
        log.info("Params: {}", params);
        log.info("Headers: {}", headers);
        log.info("Consumes: {}", consumes);
        log.info("Produces: {}", produces);
        log.info("Mapping Name: {}", mappingName);

        dynamicEndpointConfig endpointConfig = new dynamicEndpointConfig();
        endpointConfig.setConsumes(Collections.singletonList(consumes));
        endpointConfig.setProduces(Collections.singletonList(produces));
        endpointConfig.setHttpMethod(method);
        endpointConfig.setPath(paths);
        endpointConfig.setId(mappingName);
        endpointConfig.setHeaders(null);
        endpointConfig.setRequiresAuthentication(false);
        endpointConfig.setRequiredRoles(null);
        endpointConfig.setExpectedResponseBody(null);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> expectedRequestBodyMap = null;
        try {
            expectedRequestBodyMap = objectMapper.readValue(expectedRequestBody, new TypeReference<Map<String, Object>>() {});

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        endpointConfig.setExpectedRequestBody(expectedRequestBodyMap);


        dynamicEndpointService.registerEndpoint(endpointConfig);
        dynamicEndpointService.saveEndpoint(endpointConfig);
        /*

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(paths.split(","))
                .methods(RequestMethod.resolve(method))
                .params(params.split(","))
                .headers(headers.split(","))
                .consumes(consumes.split(","))
                .produces(produces.split(","))
                .mappingName(mappingName);

         */
        // Set other properties based on form fields

        // Use the RequestMappingInfo.Builder as needed (e.g., register with a handler method)

        redirectAttributes.addFlashAttribute("successMessage", "Mapping created successfully.");
        return "redirect:/request-mapping-form";
    }

    @RequestMapping("/request-mapping-form")
    public String showRequestMappingForm(Model model){
        List<String> mediaTypes = grabAllKnownTypes();
        // Populate the mediaTypes list with the desired values

        model.addAttribute("mediaTypes", mediaTypes);

        return "request-mapping-form";
    }

    @PostMapping("/apiform")
    public String submitForm(@ModelAttribute("formData") formData formData, Model model) {
        // Process the submitted form data as needed
        // ...

        // Pass the necessary data to the view for rendering
       // model.addAttribute("result", formData);
        makeIntegrationCall(formData);
        model.addAttribute("previousResponse", getPreviousResponse());
        
        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the JSON string payload
        String payload = "{\"relationshipID\":\"1\"}";

        // Create the request entity with the JSON payload
        RequestEntity<String> requestEntity = null;
        try {
            requestEntity = RequestEntity.post(new URI("http://localhost:8080/someEndpoint"))
                    .headers(headers)
                    .body(payload);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Make the REST call to the endpoint
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        // Get the response body
        String responseBody = response.getBody();
        /*

         */
       // model.addAttribute("previousResponse", formData);
        // Return the name of the Thymeleaf fragment or HTML snippet to be loaded dynamically
        return "result :: content"; // Assuming "result" is the name of the Thymeleaf template and "content" is the fragment to be loaded
    }
    // Mapping for the page with the form
    @GetMapping("/apiform")
    public String showFormPage(Model model) {
        model.addAttribute("formData", new formData()); // Initialize the form data model object
        model.addAttribute("previousResponse", getPreviousResponse());
        return "apiform"; // Return the name of the Thymeleaf template for the form page
    }

    private void makeIntegrationCall(formData formData) {
        LocalDateTime currentTime = LocalDateTime.now();
        long startTime = System.currentTimeMillis();

        // Perform integration call 2
        // ...

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        responseData.getResponseTimes().add("IntegrationCall1: " + elapsedTime + " ms");
        responseData.getResponseMessages().add(formData.toString());
        responseData.getData().add(currentTime);
    }
    private PreviousResponse getPreviousResponse() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<String> responseTimesList = responseData.getResponseTimes();
        List<String> responseMessagesList = responseData.getResponseMessages();

        return new PreviousResponse(responseTimesList, responseMessagesList, responseData.getData());
    }


}
