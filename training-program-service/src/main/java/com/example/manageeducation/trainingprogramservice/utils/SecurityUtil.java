package com.example.manageeducation.trainingprogramservice.utils;

import com.example.manageeducation.trainingprogramservice.client.CustomerClient;
import com.example.manageeducation.trainingprogramservice.dto.Customer;
import com.example.manageeducation.trainingprogramservice.dto.CustomerRequest;
import com.example.manageeducation.trainingprogramservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class SecurityUtil {

    @Autowired
    CustomerClient customerClient;

    public CustomerRequest getLoginUser(Principal principal){
        String email = principal.getName();
        CustomerRequest customer = new CustomerRequest();
        Optional<Customer> customerOptional = customerClient.getCustomerByEmail(email);
        if(customerOptional.isPresent()){
            Customer customer1 = customerOptional.get();
            customer.setId(customer1.getId());
            return customer;
        }else {
            throw new BadRequestException("Invalid token.");
        }

    }
}
