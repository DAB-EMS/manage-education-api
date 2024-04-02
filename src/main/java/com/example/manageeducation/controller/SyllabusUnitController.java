package com.example.manageeducation.controller;

import com.example.manageeducation.entity.SyllabusLevel;
import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.service.SyllabusUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/syllabus")
public class SyllabusUnitController {

    @Autowired
    SyllabusUnitService syllabusUnitService;

    @PostMapping("/syllabus-unit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createSyllabusUnit(@RequestBody SyllabusUnit dto) {
        return ResponseEntity.ok(syllabusUnitService.createSyllabusUnit(dto));
    }
}
