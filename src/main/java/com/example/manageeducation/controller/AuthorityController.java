package com.example.manageeducation.controller;

import com.example.manageeducation.enums.Resource;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthorityController {
    @Autowired
    AuthorityService authorityService;

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @GetMapping("/authorities")
    public ResponseEntity<?> authority(@RequestParam(required = true) Resource resource) {
        return ResponseEntity.ok(authorityService.syllabusAuthorities(String.valueOf(resource)));
    }

    @PreAuthorize("hasAuthority('FULL_ACCESS_USER')")
    @GetMapping("/authority")
    public ResponseEntity<?> authority(@RequestParam RoleType role, @RequestParam Resource resource) {
        return ResponseEntity.ok(authorityService.getAuthorities(role, String.valueOf(resource)));
    }
}
