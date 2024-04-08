package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.CustomerUpdateRequest;
import com.example.manageeducation.dto.response.CustomerResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.CustomerRepository;
import com.example.manageeducation.repository.RoleRepository;
import com.example.manageeducation.service.CustomerService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.sql.Types.NUMERIC;
import static org.apache.tomcat.util.bcel.classfile.ElementValue.STRING;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;
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
        List<Customer> customerList = customerRepository.findAll();
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
        try (InputStream inputStream = file.getInputStream()) {
            // Tạo workbook từ InputStream
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // Lấy ra sheet đầu tiên từ workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Tạo đối tượng FormulaEvaluator để tính giá trị của các công thức
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // Duyệt qua từng hàng trong sheet
            for (Row row : sheet) {
                // Duyệt qua từng ô trong hàng
                for (Cell cell : row) {
                    switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum()) {
                        case NUMERIC:   // Ô kiểu số
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case STRING:    // Ô kiểu chuỗi
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                }
                System.out.println();
            }
        }

        return "Customer created successfully!";
    }
}
