package com.example.demo.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SampleRepository sampleRepository;

    @Autowired
    public DataInitializer(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert sample data here
        SampleEntity entity1 = new SampleEntity();
        // Set entity1 properties
        sampleRepository.save(entity1);

        SampleEntity entity2 = new SampleEntity();
        // Set entity2 properties
        sampleRepository.save(entity2);

        // Add more sample records as needed
    }
}
