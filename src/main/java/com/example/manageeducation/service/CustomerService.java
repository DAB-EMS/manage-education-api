package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.CustomerImportRequest;
import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.dto.response.CustomerResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.enums.RoleType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CustomerService {
    Customer GetCustomerByEmail(String email);
    List<CustomerResponse> userList(String search);
    String deActiveCustomer(String customerId);
    String deleteCustomer(String customerId);
    String changeRole(String customerId, RoleType role);
    String updateCustomer(String customerId, CustomerUpdateRequest dto);
    String createCustomerByExcel(MultipartFile file) throws IOException;
}
