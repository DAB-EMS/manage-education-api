package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.CustomerResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.enums.RoleType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    Customer GetCustomerByEmail(String email);
    List<CustomerResponse> userList(String search);
    String deActiveCustomer(String customerId);
    String deleteCustomer(String customerId);
    String changeRole(String customerId, RoleType role);
}
