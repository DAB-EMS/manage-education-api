package com.example.manageeducation.controller;

import com.example.manageeducation.entity.SyllabusUnitChapter;
import com.example.manageeducation.service.SyllabusUnitChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/syllabus/")
public class SyllabusUnitChapterController {

    @Autowired
    SyllabusUnitChapterService syllabusUnitChapterService;

    @PostMapping("/syllabus-unit-chapter")
    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
    public ResponseEntity<?> createSyllabusUnitChapter(@RequestBody SyllabusUnitChapter dto) {
        return ResponseEntity.ok(syllabusUnitChapterService.createSyllabusUnitChapter(dto));
    }
}
