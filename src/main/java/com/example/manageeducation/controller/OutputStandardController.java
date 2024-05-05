package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.OutputStandardRequest;
import com.example.manageeducation.service.OutputStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/syllabus/syllabus-day/syllabus-unit/unit-chapter")
public class OutputStandardController {

    @Autowired
    OutputStandardService outputStandardService;

    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
    @PostMapping("/output-standard")
    public ResponseEntity<?> createOutputStandard(@RequestBody OutputStandardRequest dto) {
        return ResponseEntity.ok(outputStandardService.createOutputStandard(dto));
    }

    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    @PutMapping("/output-standard/{output-standard-id}")
    public ResponseEntity<?> updateOutputStandard(@PathVariable("output-standard-id") UUID id, @RequestBody OutputStandardRequest dto) {
        return ResponseEntity.ok(outputStandardService.updateOutputStandard(id,dto));
    }

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/output-standards")
    public ResponseEntity<?> outputStandards() {
        return ResponseEntity.ok(outputStandardService.outputStandards());
    }
}
