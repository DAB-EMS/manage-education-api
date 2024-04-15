package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.service.TrainingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingClassController {

    @Autowired
    TrainingClassService trainingClassService;

    @PostMapping("customer/training-program/{training-program-id}/training-program")
    public ResponseEntity<?> createTrainingProgram(Principal principal, @PathVariable("training-program-id") UUID id, @RequestBody TrainingClassRequest dto) {
        return ResponseEntity.ok(trainingClassService.createTrainingClass(principal,id,dto));
    }

    @GetMapping("customer/training-program/training-classes")
    public ResponseEntity<?> trainingProgramList() {
        return ResponseEntity.ok(trainingClassService.TrainingClassesResponses());
    }
}
