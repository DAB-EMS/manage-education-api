package com.example.manageeducation.userservice.controller;

import com.example.manageeducation.userservice.dto.CustomerImportRequest;
import com.example.manageeducation.userservice.dto.CustomerUpdateRequest;
import com.example.manageeducation.userservice.dto.RequestForListOfCustomer;
import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.model.ResponseObject;
import com.example.manageeducation.userservice.service.CustomerService;
import com.example.manageeducation.userservice.service.impl.FirebaseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    @Autowired
    FirebaseService firebaseService;

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(customerService.userList(search));
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customers/status")
    public ResponseEntity<?> getCustomerStatus() {
        List<String> customerStatusList = Arrays.stream(CustomerStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerStatusList);
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customers/genders")
    public ResponseEntity<?> getCustomerGenders() {
        List<String> customerGenderList = Arrays.stream(Gender.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerGenderList);
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customers/roles")
    public ResponseEntity<?> getCustomerRoles() {
        List<String> customerRoleList = Arrays.stream(RoleType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerRoleList);
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customers/levels")
    public ResponseEntity<?> getCustomerOthers() {
        return ResponseEntity.ok(customerService.customerLevel());
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customers/role")
    public ResponseEntity<?> getCustomerByStatus(@RequestParam(required = true) RoleType role) {
        return ResponseEntity.ok(customerService.customerByStatus(role));
    }

    @GetMapping("/customer/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        return ResponseEntity.ok(customerService.getProfile(principal));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @PostMapping("/customer")
    public ResponseEntity<?> postCustomer(@RequestBody CustomerImportRequest dto) {
        return ResponseEntity.ok(customerService.createUser(dto));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @PutMapping("/customer/{customer-id}/de-active")
    public ResponseEntity<?> putDeActiveCustomer(@PathVariable("customer-id") UUID id) {
        return ResponseEntity.ok(customerService.deActiveCustomer(id));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @PutMapping("/customer/{customer-id}/change-role")
    public ResponseEntity<?> changeRoleCustomer(@PathVariable("customer-id") UUID id, RoleType role) {
        return ResponseEntity.ok(customerService.changeRole(id,role));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @PutMapping("/customer/{customer-id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customer-id") UUID id, CustomerUpdateRequest dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id,dto));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @DeleteMapping("/customer/{customer-id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customer-id") UUID id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

//    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customer-id") UUID id) {
        return ResponseEntity.ok(customerService.getUser(id));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<?> getCustomerByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(customerService.GetCustomerByEmail(email));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "/customer/import", consumes = "multipart/form-data")
    public ResponseEntity<?> updateCustomer(@RequestPart MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(customerService.createCustomerByExcel(file));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
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

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
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

    @GetMapping("/customers/keywords")
    @Operation(summary = "get all customers ")
    public ResponseEntity<ResponseObject> searchCustomers(
            @RequestParam(name = "keyword", defaultValue = "") String keywords,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false)
            @Parameter(name = "sortBy", description = "name or age or address") String sortBy,
            @RequestParam(value = "sortType", required = false)
            @Parameter(name = "sortType", description = "ASC or DESC") String sortType) {
        RequestForListOfCustomer request = new RequestForListOfCustomer(keywords,page, size, sortBy, sortType);
        LOGGER.info("Start method List of Customers in SpringDataController");
        return customerService.getAllCustomers(request);
    }

}
