package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.TrainingProgramRequest;
import com.example.manageeducation.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingProgramController {

    @Autowired
    TrainingProgramService trainingProgramService;

    @PostMapping("customer/{customer-id}/training-program")
    public ResponseEntity<?> createTrainingProgram(@PathVariable("customer-id") String customerId, @RequestBody TrainingProgramRequest dto) {
        return ResponseEntity.ok(trainingProgramService.createTrainingProgram(customerId,dto));
    }

    @GetMapping("customer/training-program/{training-program-id}")
    public ResponseEntity<?> createTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.viewTrainingProgram(id));
    }

    @PutMapping("customer/training-program/{training-program-id}")
    public ResponseEntity<?> inActiveTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.deActiveTrainingProgram(id));
    }

    @PostMapping("customer/{customer-id}/training-program/{training-program-id}")
    public ResponseEntity<?> duplicatedTrainingProgram(@PathVariable("customer-id") String customerId, @PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.duplicatedTrainingProgram(customerId,id));
    }
}
