package com.example.manageeducation.controller;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.service.SyllabusDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/syllabus")
public class SyllabusDayController {

    @Autowired
    SyllabusDayService syllabusDayService;

    @PostMapping("/syllabus-day")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createSyllabusDay(@RequestBody SyllabusDay dto) {
        return ResponseEntity.ok(syllabusDayService.createSyllabusDay(dto));
    }
}
