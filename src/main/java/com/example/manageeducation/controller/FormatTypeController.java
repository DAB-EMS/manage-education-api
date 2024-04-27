package com.example.manageeducation.controller;

import com.example.manageeducation.entity.FormatType;
import com.example.manageeducation.service.FormatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class FormatTypeController {

    @Autowired
    FormatTypeService formatTypeService;

    @GetMapping("/format-types")
    public ResponseEntity<?> attendLevels() {
        return ResponseEntity.ok(formatTypeService.formatTypeResponse());
    }
}
