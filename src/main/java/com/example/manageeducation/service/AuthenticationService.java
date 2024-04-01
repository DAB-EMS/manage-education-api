package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.AuthenticationRequest;
import com.example.manageeducation.dto.request.RegisterRequest;
import com.example.manageeducation.dto.response.AuthenticationResponse;
import com.example.manageeducation.entity.Customer;
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
