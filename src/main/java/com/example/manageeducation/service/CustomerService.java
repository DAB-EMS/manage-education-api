package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.CustomerImportRequest;
import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.dto.response.CustomerResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.enums.RoleType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public interface CustomerService {
    Customer GetCustomerByEmail(String email);
    List<CustomerResponse> userList(String search);
    String deActiveCustomer(UUID customerId);
    String deleteCustomer(UUID customerId);
    String changeRole(UUID customerId, RoleType role);
    String updateCustomer(UUID customerId, CustomerUpdateRequest dto);
    String createCustomerByExcel(MultipartFile file) throws IOException;
    CustomerResponse getUser(UUID Id);
    String createUser(CustomerImportRequest dto);
    List<CustomerResponse> customerByStatus(RoleType role);
    HashSet<String> customerLevel();
    CustomerResponse getProfile(Principal principal);

}
