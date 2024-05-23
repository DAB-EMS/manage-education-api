package com.example.manageeducation.userservice.service.impl;

import com.example.manageeducation.userservice.dto.AuthenticationResponse;
import com.example.manageeducation.userservice.dto.RegisterRequest;
import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.model.Customer;
import com.example.manageeducation.userservice.repository.CustomerRepository;
import com.example.manageeducation.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        java.time.Instant instant = utilDate.toInstant();
        var user = Customer.builder()
                .fullName(request.getName())
                .avatar(request.getAvatar())
                .birthday(request.getBirthday())
                .createdDate(instant)
                .status(CustomerStatus.ACTIVE)
                .level(request.getLevel())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender()==null? Gender.MALE:request.getGender())
                .role(request.getRole())//Role.USER
                .build();
        var savedUser = customerRepository.save(modelMapper.map(user, Customer.class));
        return AuthenticationResponse.builder()
                .role(String.valueOf(savedUser.getRole().getName()))
                .build();
    }

}
