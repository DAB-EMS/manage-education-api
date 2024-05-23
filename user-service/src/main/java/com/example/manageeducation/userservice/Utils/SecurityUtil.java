package com.example.manageeducation.userservice.Utils;

import com.example.manageeducation.userservice.dto.CustomerRequest;
import com.example.manageeducation.userservice.exception.BadRequestException;
import com.example.manageeducation.userservice.model.Customer;
import com.example.manageeducation.userservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class SecurityUtil {

    @Autowired
    CustomerRepository customerRepository;

    public CustomerRequest getLoginUser(Principal principal){
        String email = principal.getName();
        CustomerRequest customer = new CustomerRequest();
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if(customerOptional.isPresent()){
            Customer customer1 = customerOptional.get();
            customer.setId(customer1.getId());
            return customer;
        }else {
            throw new BadRequestException("Invalid token.");
        }

    }
}
