package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.SyllabusRequest;
import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.service.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/syllabus")
public class SyllabusController {

    @Autowired
    SyllabusService syllabusService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createSyllabus(@RequestBody SyllabusRequest dto) {
        return ResponseEntity.ok(syllabusService.createSyllabus(dto));
    }
}
