package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.CustomerImportRequest;
import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.service.CustomerService;
import com.example.manageeducation.service.impl.FirebaseService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping("/customers/status")
    public ResponseEntity<?> getCustomerStatus() {
        List<String> customerStatusList = Arrays.stream(CustomerStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerStatusList);
    }

    @GetMapping("/customers/genders")
    public ResponseEntity<?> getCustomerGenders() {
        List<String> customerGenderList = Arrays.stream(Gender.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerGenderList);
    }

    @GetMapping("/customers/roles")
    public ResponseEntity<?> getCustomerRoles() {
        List<String> customerRoleList = Arrays.stream(RoleType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerRoleList);
    }

    @GetMapping("/customers/levels")
    public ResponseEntity<?> getCustomerOthers() {
        return ResponseEntity.ok(customerService.customerLevel());
    }

    @GetMapping("/customers/role")
    public ResponseEntity<?> getCustomerByStatus(@RequestParam(required = true) RoleType role) {
        return ResponseEntity.ok(customerService.customerByStatus(role));
    }

    @GetMapping("/customer/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        return ResponseEntity.ok(customerService.getProfile(principal));
    }

    @PostMapping("/customer")
    public ResponseEntity<?> postCustomer(@RequestBody CustomerImportRequest dto) {
        return ResponseEntity.ok(customerService.createUser(dto));
    }

    @PutMapping("/customer/{customer-id}/de-active")
    public ResponseEntity<?> putDeActiveCustomer(@PathVariable("customer-id") UUID id) {
        return ResponseEntity.ok(customerService.deActiveCustomer(id));
    }

    @PutMapping("/customer/{customer-id}/change-role")
    public ResponseEntity<?> changeRoleCustomer(@PathVariable("customer-id") UUID id, RoleType role) {
        return ResponseEntity.ok(customerService.changeRole(id,role));
    }

    @PutMapping("/customer/{customer-id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customer-id") UUID id, CustomerUpdateRequest dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id,dto));
    }

    @DeleteMapping("/customer/{customer-id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customer-id") UUID id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customer-id") UUID id) {
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

    @GetMapping("customer/template/download")
    public @ResponseBody byte[] downloadCsvTemplate(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"UserTemplate.zip\"");
        InputStream file;
        try {
            file = getClass().getResourceAsStream("/templates/UserTemplate.zip");
        } catch (Exception e) {
            throw new FileNotFoundException("File template not exist");
        }
        return IOUtils.toByteArray(file);
    }

}
