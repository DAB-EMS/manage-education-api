package com.example.manageeducation.userservice.controller;

import com.example.manageeducation.userservice.dto.AuthenticationRequest;
import com.example.manageeducation.userservice.dto.AuthenticationResponse;
import com.example.manageeducation.userservice.dto.RegisterRequest;
import com.example.manageeducation.userservice.model.Customer;
import com.example.manageeducation.userservice.service.AuthenticationService;
import com.example.manageeducation.userservice.service.CustomerService;
import com.example.manageeducation.userservice.service.RoleService;
import com.example.manageeducation.userservice.service.impl.FirebaseService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CustomerService customerService;

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    RoleService roleService;

    @PostMapping("/firebase")
    public ResponseEntity<AuthenticationResponse> authorizeToken(@RequestBody String token) {
        try {
            FirebaseToken decodedToken = firebaseService.verifyToken(token);

            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();
            Customer customer = customerService.GetCustomerByEmail(email);
            if(customer !=null){
                AuthenticationRequest request = new AuthenticationRequest();
                request.setEmail(email);
                request.setPassword("");
                return ResponseEntity.ok(authenticationService.authenticate(request));
            }else{
                RegisterRequest request = new RegisterRequest();
                request.setId(uid);
                request.setName(name);
                request.setAvatar(picture);
                request.setEmail(email);
                request.setPassword("");
                request.setRole(roleService.getRoleByName());

                return ResponseEntity.ok(authenticationService.register(request));
            }
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse());
        }

    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request,response);
    }
}
