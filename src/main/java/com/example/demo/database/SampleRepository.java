package com.example.demo.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
    // Define additional custom queries if needed
}
