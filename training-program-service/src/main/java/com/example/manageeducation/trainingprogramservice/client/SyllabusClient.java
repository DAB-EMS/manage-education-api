package com.example.manageeducation.trainingprogramservice.client;

import com.example.manageeducation.trainingprogramservice.dto.Customer;
import com.example.manageeducation.trainingprogramservice.dto.Syllabus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@Component
@FeignClient(name = "user-service", url = "${application.config.program-url}")
public interface SyllabusClient {
    @GetMapping("/customer/{customer-id}")
    Optional<Syllabus> getCustomerById(@PathVariable("customer-id") UUID id);
}
