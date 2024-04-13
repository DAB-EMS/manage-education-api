package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.CustomerImportRequest;
import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.dto.request.RegisterRequest;
import com.example.manageeducation.dto.response.CustomerResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.CustomerRepository;
import com.example.manageeducation.repository.RoleRepository;
import com.example.manageeducation.service.AuthenticationService;
import com.example.manageeducation.service.CustomerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;
    @Override
    public Customer GetCustomerByEmail(String email) {
        try{
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if(customerOptional.isPresent()){
                return customerOptional.get();
            }else{
                throw new BadRequestException("Email not exist in system.");
            }
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<CustomerResponse> userList(String search) {
        List<Customer> customerList;
        if (search != null && !search.isEmpty()) {
            customerList = customerRepository.findByFullNameContainingIgnoreCase(search);
        } else {
            customerList = customerRepository.findAllByStatus(CustomerStatus.ACTIVE);
        }
        return customerList.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }


    @Override
    public String deActiveCustomer(String customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setStatus(CustomerStatus.DEACTIVE);
            customerRepository.save(customer);
            return "De-active customer successful.";
        }else{
            throw new BadRequestException("Customer id is not found.");
        }
    }

    @Override
    public String deleteCustomer(String customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setStatus(CustomerStatus.DELETE);
            customerRepository.save(customer);
            return "Delete customer successful.";
        }else{
            throw new BadRequestException("Customer id is not found.");
        }
    }

    @Override
    public String changeRole(String customerId, RoleType role) {
        Optional<Role> roleOptional = roleRepository.findByName(role);
        if(roleOptional.isEmpty()){
            throw new BadRequestException("Role is not exist.");
        }
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            customer.setRole(roleOptional.get());
            customerRepository.save(customer);
            return "Change role customer successful.";
        }else{
            throw new BadRequestException("Customer id is not found.");
        }
    }

    @Override
    public String updateCustomer(String customerId, CustomerUpdateRequest dto) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            modelMapper.map(customer,dto);
            customerRepository.save(customer);
            return "Update customer successful.";
        }else{
            throw new BadRequestException("Customer id is not found.");
        }
    }

    @Override
    public String createCustomerByExcel(MultipartFile file) throws IOException {
        List<CustomerImportRequest> importRequests = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                CustomerImportRequest importRequest = new CustomerImportRequest();

                importRequest.setFullName(getStringCellValue(row.getCell(1), formulaEvaluator));
                importRequest.setBirthday(dateFormat.parse(getStringCellValue(row.getCell(2), formulaEvaluator)));
                if(getStringCellValue(row.getCell(3), formulaEvaluator)!=null){
                    if(getStringCellValue(row.getCell(3), formulaEvaluator).equalsIgnoreCase("Male")){
                        importRequest.setGender(Gender.MALE);
                    }else if(getStringCellValue(row.getCell(3), formulaEvaluator).equalsIgnoreCase("Female")){
                        importRequest.setGender(Gender.FEMALE);
                    }else{
                        importRequest.setGender(Gender.FEMALE);
                    }
                }else {
                    importRequest.setGender(Gender.FEMALE);
                }
                importRequest.setEmail(getStringCellValue(row.getCell(4), formulaEvaluator));
                //check email exist in system
                Optional<Customer> customerOptional = customerRepository.findByEmail(getStringCellValue(row.getCell(4), formulaEvaluator));
                if(customerOptional.isPresent()){
                    throw new BadRequestException("Email " + customerOptional.get().getEmail() + " existed in system.");
                }

                //check validation password
                importRequest.setPassword(getStringCellValue(row.getCell(5), formulaEvaluator));
                if(getStringCellValue(row.getCell(5), formulaEvaluator).length()<6){
                    throw new BadRequestException("Password have to large than or equal 6.");
                }

                //check validation role
                if(getStringCellValue(row.getCell(6), formulaEvaluator).equalsIgnoreCase("STUDENT")){
                    importRequest.setRole(RoleType.USER);
                }else if(getStringCellValue(row.getCell(6), formulaEvaluator).equalsIgnoreCase("CLASS_ADMIN")){
                    importRequest.setRole(RoleType.ADMIN);
                }else if(getStringCellValue(row.getCell(6), formulaEvaluator).equalsIgnoreCase("TRAINER")){
                    importRequest.setRole(RoleType.TRAINER);
                }else{
                    importRequest.setRole(RoleType.USER);
                }
                importRequest.setLevel(getStringCellValue(row.getCell(7), formulaEvaluator));

                importRequests.add(importRequest);
                createCustomerWithSystem(importRequests);
            }
            return "Create customer successful.";
        } catch (ParseException e) {
            throw new BadRequestException("Please fill in all information and use the correct excel file downloaded from the system.");
        }
    }

    private boolean createCustomerWithSystem(List<CustomerImportRequest> customers){
        try{
            for(CustomerImportRequest customer: customers){
                createCustomerWithFirebase(customer);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void createCustomerWithFirebase(CustomerImportRequest customer){
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(customer.getEmail())
                    .setEmailVerified(false)
                    .setPassword(customer.getPassword())
                    .setDisplayName(customer.getFullName())
                    .setDisabled(false);

            UserRecord userRecord = auth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setId(userRecord.getUid());
            registerRequest.setName(customer.getFullName());
            Optional<Role> roleOptional = roleRepository.findByName(customer.getRole());
            if(roleOptional.isPresent()){
                registerRequest.setRole(roleOptional.get());
            }else{
                throw new BadRequestException("Role id is not found.");
            }
            modelMapper.map(customer,registerRequest);
            authenticationService.register(registerRequest);
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }

    }

    private String getStringCellValue(Cell cell, FormulaEvaluator formulaEvaluator) {
        if (cell == null) {
            return null;
        }
        switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    cell.setCellType(CellType.STRING);
                    return cell.getStringCellValue();
                }
            default:
                return null;
        }
    }
}
