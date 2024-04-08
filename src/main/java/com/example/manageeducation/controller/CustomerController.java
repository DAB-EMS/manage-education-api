package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(String search) {
        return ResponseEntity.ok(customerService.userList(search));
    }

    @PutMapping("/customer/{customer-id}/de-active")
    public ResponseEntity<?> putDeActiveCustomer(@PathVariable("customer-id") String id) {
        return ResponseEntity.ok(customerService.deActiveCustomer(id));
    }

    @PutMapping("/customer/{customer-id}/change-role")
    public ResponseEntity<?> changeRoleCustomer(@PathVariable("customer-id") String id, RoleType role) {
        return ResponseEntity.ok(customerService.changeRole(id,role));
    }

    @PutMapping("/customer/{customer-id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customer-id") String id, CustomerUpdateRequest dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id,dto));
    }

    @DeleteMapping("/customer/{customer-id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customer-id") String id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
