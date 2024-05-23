package com.example.manageeducation.userservice.service;

import com.example.manageeducation.userservice.dto.AuthenticationRequest;
import com.example.manageeducation.userservice.dto.AuthenticationResponse;
import com.example.manageeducation.userservice.dto.RegisterRequest;
import com.example.manageeducation.userservice.model.Customer;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void saveUserToken(Customer customer, String jwtToken);

    void revokeAllUserTokens(Customer customer);
    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
