package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.SyllabusUnitAddRequest;
import com.example.manageeducation.entity.SyllabusLevel;
import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.service.SyllabusUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/syllabus")
public class SyllabusUnitController {

    @Autowired
    SyllabusUnitService syllabusUnitService;

    @PostMapping("/{syllabus-id}/syllabus-day/{syllabus-day-id}/syllabus-unit")
    public ResponseEntity<?> createSyllabusUnit(@PathVariable("syllabus-id") UUID syllabusId, @PathVariable("syllabus-day-id") UUID dayId, @RequestBody SyllabusUnitAddRequest dto) {
        return ResponseEntity.ok(syllabusUnitService.createSyllabusUnit(syllabusId,dayId,dto));
    }

    @PutMapping("/syllabus-day/syllabus-unit/{syllabus-unit-id}")
    public ResponseEntity<?> createSyllabusUnit(@PathVariable("syllabus-unit-id") UUID unitId, @RequestBody SyllabusUnitAddRequest dto) {
        return ResponseEntity.ok(syllabusUnitService.updateSyllabusUnit(unitId,dto));
    }
}
