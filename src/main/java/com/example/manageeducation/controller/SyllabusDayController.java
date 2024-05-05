package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.SyllabusDayAddRequest;
import com.example.manageeducation.dto.request.SyllabusDayRequest;
import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.service.SyllabusDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/syllabus")
public class SyllabusDayController {

    @Autowired
    SyllabusDayService syllabusDayService;

    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
    @PostMapping("/{syllabus-id}/syllabus-day")
    public ResponseEntity<?> createSyllabusDay(@PathVariable("syllabus-id") UUID id, @RequestBody SyllabusDayAddRequest dto) {
        return ResponseEntity.ok(syllabusDayService.createSyllabusDay(id,dto));
    }

    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    @DeleteMapping("/syllabus-day/{syllabus-day-id}")
    public ResponseEntity<?> deleteSyllabusDay(@PathVariable("syllabus-day-id") UUID id) {
        return ResponseEntity.ok(syllabusDayService.deleteSyllabusDay(id));
    }

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/{syllabus-id}/syllabus-days")
    public ResponseEntity<?> getSyllabusDay(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusDayService.syllabusDays(id));
    }
}
