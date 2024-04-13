package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.CustomerImportRequest;
import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.service.CustomerService;
import com.example.manageeducation.service.impl.FirebaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    FirebaseService firebaseService;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(customerService.userList(search));
    }

    @PostMapping("/customer")
    public ResponseEntity<?> postCustomer(@RequestBody CustomerImportRequest dto) {
        return ResponseEntity.ok(customerService.createUser(dto));
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

    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customer-id") String id) {
        return ResponseEntity.ok(customerService.getUser(id));
    }

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "/customer/import", consumes = "multipart/form-data")
    public ResponseEntity<?> updateCustomer(@RequestPart MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(customerService.createCustomerByExcel(file));
    }

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "/upload/image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file) {
        try {
            String fileName = firebaseService.pushImage(file);
            return ResponseEntity.ok(fileName);
        } catch (Exception e) {
            //  throw internal error;
        }
        return ResponseEntity.ok().build();
    }
}
