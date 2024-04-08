package com.example.manageeducation.controller;

import com.example.manageeducation.Utils.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/con")
public class testController {

    @GetMapping("/information/config")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAdminConfigurationInfo() throws JsonProcessingException {
        return ResponseEntity.ok("Good");
    }

    @Autowired
    SecurityUtil securityUtil;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Principal principal) {
        return ResponseEntity.ok(securityUtil.getLoginUser(principal));
    }

}
