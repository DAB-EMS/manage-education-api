package com.example.manageeducation.syllabusservice.Utils;

import com.example.manageeducation.dto.request.CustomerRequest;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.CustomerRepository;
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
