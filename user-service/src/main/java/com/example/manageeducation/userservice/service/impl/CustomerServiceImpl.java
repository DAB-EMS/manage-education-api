package com.example.manageeducation.userservice.service.impl;

import com.example.manageeducation.userservice.Utils.CustomerServiceUtils;
import com.example.manageeducation.userservice.Utils.SecurityUtil;
import com.example.manageeducation.userservice.controller.CustomerController;
import com.example.manageeducation.userservice.dto.*;
import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.exception.BadRequestException;
import com.example.manageeducation.userservice.jdbc.CustomerJdbc;
import com.example.manageeducation.userservice.model.Customer;
import com.example.manageeducation.userservice.model.Pagination;
import com.example.manageeducation.userservice.model.ResponseObject;
import com.example.manageeducation.userservice.model.Role;
import com.example.manageeducation.userservice.repository.CustomerRepository;
import com.example.manageeducation.userservice.repository.RoleRepository;
import com.example.manageeducation.userservice.service.AuthenticationService;
import com.example.manageeducation.userservice.service.CustomerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerJdbc customerJdbc;

    @Autowired
    CustomerServiceUtils customerServiceUtils;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    SecurityUtil securityUtil;
    @Override
    public Customer GetCustomerByEmail(String email) {
        try{
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if(customerOptional.isPresent()){
                return customerOptional.get();
            }else{
                return null;
            }
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
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
    public String deActiveCustomer(UUID customerId) {
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
    public String deleteCustomer(UUID customerId) {
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
    public String changeRole(UUID customerId, RoleType role) {
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
    public String updateCustomer(UUID customerId, CustomerUpdateRequest dto) {
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
                    importRequest.setRole(RoleType.STUDENT);
                }else if(getStringCellValue(row.getCell(6), formulaEvaluator).equalsIgnoreCase("CLASS_ADMIN")){
                    importRequest.setRole(RoleType.CLASS_ADMIN);
                }else if(getStringCellValue(row.getCell(6), formulaEvaluator).equalsIgnoreCase("TRAINER")){
                    importRequest.setRole(RoleType.TRAINER);
                }else{
                    importRequest.setRole(RoleType.STUDENT);
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

    @Override
    public CustomerResponse getUser(UUID Id) {
        Optional<Customer> customerOptional = customerRepository.findById(Id);
        if(customerOptional.isPresent()){
            return modelMapper.map(customerOptional.get(),CustomerResponse.class);
        }else {
            throw new BadRequestException("Customer id is not found.");
        }
    }

    @Override
    public String createUser(CustomerImportRequest dto) {

        if(dto.getPassword().length()<6){
            throw new BadRequestException("Password is large than or equal 6.");
        }

        Optional<Customer> customerOptional = customerRepository.findByEmail(dto.getEmail());
        if(customerOptional.isPresent()){
            throw new BadRequestException("Email is existed.");
        }

        createCustomerWithFirebase(dto);
        return "Create successful.";
    }

    @Override
    public List<CustomerResponse> customerByStatus(RoleType role) {
        List<Customer> customers = customerRepository.findAllByRole_Name(role);
        return customers
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public HashSet<String> customerLevel() {
        HashSet<String> levels = new HashSet<>();
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            if(customer.getLevel()!=null){
                levels.add(customer.getLevel());
            }
        }
        return levels;
    }

    @Override
    public CustomerResponse getProfile(Principal principal) {
        Optional<Customer> customerOptional = customerRepository.findById(securityUtil.getLoginUser(principal).getId());
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            return modelMapper.map(customer,CustomerResponse.class);
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> getAllCustomers(RequestForListOfCustomer request) {
        LOGGER.info("Start method getAllCustomers in CustomerServiceImpl");
        List<CustomerResponse> responseData = new ArrayList<>();
        List<Customer> results = new ArrayList<>();
        int totalPage = 0;
        int totalRows = 0;
        String message = "";
        if("".equalsIgnoreCase(request.getKeyword())){
            if (request.getSortBy() == null && request.getSortType() == null) {
                LOGGER.info("Start View All Customers");
                Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
                Page<Customer> syllabusPage = customerRepository.findAll(pageable);
                totalPage = syllabusPage.getTotalPages();
                results = syllabusPage.getContent();
            }else {
                LOGGER.info("View All Customers With Sort Options");
                results = customerJdbc.getCustomers(customerServiceUtils
                        .getSQLForSortingAllCustomers(request.getPage() - 1, request.getSize(),
                                request.getSortBy(), request.getSortType()));
                totalRows = customerRepository.getTotalRows();
            }
        }else{
            if (request.getSortBy() != null && request.getSortType() != null) {
                LOGGER.info("View All Customers With Keywords and Sort Options");
                results = customerJdbc.getCustomers(customerServiceUtils
                        .getSQLForSearchingByKeywordsForSuggestions(request.getPage() - 1, request.getSize(),
                                request.getKeyword()));
                totalRows = customerRepository.getTotalRows();
            }else {
                LOGGER.info("View All Customers With Keywords");
                results = customerJdbc.getCustomers(customerServiceUtils
                        .getSQLForSearchingByKeywordsForSuggestionsAndSorting(request.getPage() - 1, request.getSize(),
                                request.getSortBy(), request.getSortType(), request.getKeyword()));
                totalRows = customerRepository.getTotalRows();
            }
        }

        if (results.size() > 0) {
            if(totalPage == 0) {
                totalPage = (int)(totalRows % request.getSize() == 0 ? (totalRows / request.getSize()) : (totalRows / request.getSize()) + 1);
            }
            message += "Total " + results.size() + " element(s) in page " + request.getPage();
            responseData = results.stream()
                    .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                    .collect(Collectors.toList());
        } else {
            LOGGER.info("Null result");
            message += "Empty list";
        }
        LOGGER.info("End method getAllCustomers in SpringDataServiceImpl");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.toString(), message,
                new Pagination(request.getPage(), request.getSize(), totalPage), responseData));
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
