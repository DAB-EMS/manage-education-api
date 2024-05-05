package com.example.manageeducation.controller;

import com.example.manageeducation.service.ClassStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ClassStatusController {
    @Autowired
    ClassStatusService classStatusService;

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/class-status")
    public ResponseEntity<?> classStatus() {
        return ResponseEntity.ok(classStatusService.classStatusResponse());
    }
}
