package com.example.manageeducation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/con")
public class testController {

    @GetMapping("/information/config")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAdminConfigurationInfo() throws JsonProcessingException {
        return ResponseEntity.ok("Good");
    }

}
