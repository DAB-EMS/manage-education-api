package com.example.manageeducation.trainingclassservice.controller;

import com.example.manageeducation.trainingclassservice.service.TechnicalGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class TechnicalGroupController {

    @Autowired
    TechnicalGroupService technicalGroupService;

    @PreAuthorize("hasAuthority('VIEW_CLASS')")
    @GetMapping("/technical-groups")
    public ResponseEntity<?> technicalGroups() {
        return ResponseEntity.ok(technicalGroupService.technicalGroupResponse());
    }
}
