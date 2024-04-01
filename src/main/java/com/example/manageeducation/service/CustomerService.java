package com.example.manageeducation.service;

import com.example.manageeducation.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    Customer GetCustomerByEmail(String email);
}
