package com.example.manageeducation.userservice.service;

import com.example.manageeducation.userservice.dto.AuthenticationResponse;
import com.example.manageeducation.userservice.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

}
