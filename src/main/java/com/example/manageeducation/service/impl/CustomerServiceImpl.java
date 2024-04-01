package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.CustomerRepository;
import com.example.manageeducation.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
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
}
