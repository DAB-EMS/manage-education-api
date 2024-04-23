package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.AuthenticationRequest;
import com.example.manageeducation.dto.request.RegisterRequest;
import com.example.manageeducation.dto.response.AuthenticationResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.entity.RefreshToken;
import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import com.example.manageeducation.enums.TokenType;
import com.example.manageeducation.repository.CustomerRepository;
import com.example.manageeducation.repository.RefreshTokenRepository;
import com.example.manageeducation.security.JwtService;
import com.example.manageeducation.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    JwtService jwtService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        java.time.Instant instant = utilDate.toInstant();
        var user = Customer.builder()
                .id(request.getId())
                .fullName(request.getName())
                .avatar(request.getAvatar())
                .birthday(request.getBirthday())
                .createdDate(instant)
                .status(CustomerStatus.ACTIVE)
                .level(request.getLevel())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender()==null?Gender.MALE:request.getGender())
                .role(request.getRole())//Role.USER
                .build();
        var savedUser = customerRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = customerRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void saveUserToken(Customer customer, String jwtToken) {
        var token = RefreshToken.builder()
                .customer(customer)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expiryDate(false)
                .revoked(false)
                .build();
        refreshTokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(Customer customer) {
        List<RefreshToken> validUserTokens = refreshTokenRepository.findAllValidTokenByUser(customer.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpiryDate(true);
            token.setRevoked(true);
        });
        refreshTokenRepository.saveAll(validUserTokens);

    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.customerRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
