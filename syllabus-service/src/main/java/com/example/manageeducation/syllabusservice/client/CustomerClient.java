package com.example.manageeducation.syllabusservice.client;

import com.example.manageeducation.syllabusservice.dto.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@Component
@FeignClient(name = "user-service", url = "${application.config.customer-url}")
public interface CustomerClient {
    @GetMapping("/customer/{customer-id}")
    Optional<Customer> getCustomerById(@PathVariable("customer-id") UUID id);

    @GetMapping("/customer/{email}")
    Optional<Customer> getCustomerByEmail(@PathVariable("email") String email);
}
