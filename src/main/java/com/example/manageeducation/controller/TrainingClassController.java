package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.service.TrainingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingClassController {

    @Autowired
    TrainingClassService trainingClassService;

    @PostMapping("customer/{customer-id}/training-class/{training-class-id}/training-program/1")
    public ResponseEntity<?> createTrainingProgram(@PathVariable("customer-id") String customerId, @PathVariable("training-class-id") UUID id, @RequestBody TrainingClassRequest dto) {
        return ResponseEntity.ok(trainingClassService.createTrainingClass(customerId,id,dto));
    }
}
