package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.TrainingProgramRequest;
import com.example.manageeducation.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingProgramController {

    @Autowired
    TrainingProgramService trainingProgramService;

    @PostMapping("customer/{customer-id}/training-program")
    public ResponseEntity<?> createTrainingProgram(@PathVariable("customer-id") String customerId, @RequestBody TrainingProgramRequest dto) {
        return ResponseEntity.ok(trainingProgramService.createTrainingProgram(customerId,dto));
    }
}
