package com.example.manageeducation.trainingclassservice.utils;

import com.example.manageeducation.trainingclassservice.client.CustomerClient;
import com.example.manageeducation.trainingclassservice.dto.Customer;
import com.example.manageeducation.trainingclassservice.dto.request.CustomerRequest;
import com.example.manageeducation.trainingclassservice.exception.BadRequestException;
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
