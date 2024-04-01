package com.example.manageeducation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class testController {

    @GetMapping("/information/config")
    public ResponseEntity<?> getAdminConfigurationInfo() throws JsonProcessingException {
        return ResponseEntity.ok("Good");
    }

}
