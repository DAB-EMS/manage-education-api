package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.SyllabusRequest;
import com.example.manageeducation.dto.request.SyllabusUpdateRequest;
import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.service.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class SyllabusController {

    @Autowired
    SyllabusService syllabusService;

    @PostMapping("/customer/{customer-id}/syllabus")
    public ResponseEntity<?> createSyllabus(@PathVariable("customer-id") String id, @RequestBody SyllabusRequest dto) {
        return ResponseEntity.ok(syllabusService.createSyllabus(id,dto));
    }

    @PostMapping("/customer/syllabus/{syllabus-id}/duplicated")
    public ResponseEntity<?> duplicatedSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.duplicatedSyllabus(id));
    }

    @GetMapping("/{syllabus-id}")
    public ResponseEntity<?> getSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.syllabus(id));
    }

    @GetMapping("/syllabus")
    public ResponseEntity<?> getSyllabuses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Date date) {
        return ResponseEntity.ok(syllabusService.syllabuses(search, date));
    }

    @DeleteMapping("/customer/syllabus/{syllabus-id}")
    public ResponseEntity<?> deleteSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.deleteSyllabus(id));
    }

    @PutMapping("/customer/syllabus/{syllabus-id}")
    public ResponseEntity<?> putSyllabus(@PathVariable("syllabus-id") UUID id, @RequestBody SyllabusUpdateRequest dto) {
        return ResponseEntity.ok(syllabusService.updateSyllabus(id,dto));
    }

    @PutMapping("/customer/syllabus/{syllabus-id}/de-active")
    public ResponseEntity<?> deActiveSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.deActive(id));
    }
}
