package com.example.manageeducation.syllabusservice.controller;

import com.example.manageeducation.syllabusservice.model.SyllabusUnitChapter;
import com.example.manageeducation.syllabusservice.service.SyllabusUnitChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
