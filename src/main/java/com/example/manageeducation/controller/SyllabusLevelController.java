package com.example.manageeducation.controller;

import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.entity.SyllabusLevel;
import com.example.manageeducation.service.SyllabusLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/syllabus")
public class SyllabusLevelController {

    @Autowired
    SyllabusLevelService syllabusLevelService;

    @PostMapping("/syllabus-level")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createSyllabusLevel(@RequestBody SyllabusLevel dto) {
        return ResponseEntity.ok(syllabusLevelService.createSyllabusLevel(dto));
    }
}
