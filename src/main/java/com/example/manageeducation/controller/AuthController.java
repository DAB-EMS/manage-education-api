package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.AuthenticationRequest;
import com.example.manageeducation.dto.request.RegisterRequest;
import com.example.manageeducation.dto.response.AuthenticationResponse;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.service.AuthenticationService;
import com.example.manageeducation.service.CustomerService;
import com.example.manageeducation.service.RoleService;
import com.example.manageeducation.service.impl.FirebaseService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
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
            // Xác thực token
            FirebaseToken decodedToken = firebaseService.verifyToken(token);

            String uid = decodedToken.getUid();
            UUID id = UUID.fromString(uid);
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();
            Customer customer = customerService.GetCustomerByEmail(email);
            if(customer !=null){
                AuthenticationRequest request = new AuthenticationRequest();
                request.setEmail(email);
                request.setPassword("");
                return ResponseEntity.ok(authenticationService.authenticate(request));
                //authenticationService.authenticate();
            }else{
                RegisterRequest request = new RegisterRequest();
                request.setId(id);
                request.setName(name);
                request.setAvatar(picture);
                request.setEmail(email);
                request.setPassword("");
                UUID roleId = UUID.fromString("your_uuid_here");
                request.setRole(roleService.GetRoleById(roleId));

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
