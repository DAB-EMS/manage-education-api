package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.OutputStandardRequest;
import com.example.manageeducation.service.OutputStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/syllabus/syllabus-day/syllabus-unit/unit-chapter")
public class OutputStandardController {

    @Autowired
    OutputStandardService outputStandardService;

    @PostMapping("/output-standard")
    public ResponseEntity<?> createOutputStandard(@RequestBody OutputStandardRequest dto) {
        return ResponseEntity.ok(outputStandardService.createOutputStandard(dto));
    }

    @PutMapping("/output-standard/{output-standard-id}")
    public ResponseEntity<?> updateOutputStandard(@PathVariable("output-standard-id") UUID id, @RequestBody OutputStandardRequest dto) {
        return ResponseEntity.ok(outputStandardService.updateOutputStandard(id,dto));
    }

    @GetMapping("/output-standards")
    public ResponseEntity<?> outputStandards() {
        return ResponseEntity.ok(outputStandardService.outputStandards());
    }
}
