package com.example.manageeducation.trainingclassservice.controller;

import com.example.manageeducation.trainingclassservice.service.AttendLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AttendLevelController {

    @Autowired
    AttendLevelService attendLevelService;

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/attend-levels")
    public ResponseEntity<?> attendLevels() {
        return ResponseEntity.ok(attendLevelService.attendLevels());
    }

}
