package com.example.manageeducation.controller;

import com.example.manageeducation.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/all-role")
    public ResponseEntity<?> attendLevels() {
        return ResponseEntity.ok(roleService.roles());
    }
}
