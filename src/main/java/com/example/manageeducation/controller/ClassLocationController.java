package com.example.manageeducation.controller;

import com.example.manageeducation.service.ClassLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ClassLocationController {

    @Autowired
    ClassLocationService classLocationService;

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/class-locations")
    public ResponseEntity<?> classLocation() {
        return ResponseEntity.ok(classLocationService.classLocationResponses());
    }
}
