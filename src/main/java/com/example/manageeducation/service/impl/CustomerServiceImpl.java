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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
