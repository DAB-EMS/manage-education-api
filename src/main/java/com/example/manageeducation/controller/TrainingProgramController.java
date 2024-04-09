package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.TrainingProgramRequest;
import com.example.manageeducation.service.TrainingProgramService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingProgramController {

    @Autowired
    TrainingProgramService trainingProgramService;

    @PostMapping("customer/training-program")
    public ResponseEntity<?> createTrainingProgram(Principal principal, @RequestBody TrainingProgramRequest dto) {
        return ResponseEntity.ok(trainingProgramService.createTrainingProgram(principal,dto));
    }

    @GetMapping("customer/training-program/{training-program-id}")
    public ResponseEntity<?> createTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.viewTrainingProgram(id));
    }

    @GetMapping("customer/training-program")
    public ResponseEntity<?> TrainingPrograms() {
        return ResponseEntity.ok(trainingProgramService.trainingPrograms());
    }

    @PutMapping("customer/training-program/{training-program-id}")
    public ResponseEntity<?> inActiveTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.deActiveTrainingProgram(id));
    }

    @PostMapping("customer/training-program/{training-program-id}/duplicated")
    public ResponseEntity<?> duplicatedTrainingProgram(Principal principal, @PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.duplicatedTrainingProgram(principal,id));
    }

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "customer/training-program/import", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(trainingProgramService.importTrainingProgram(file));
        } catch (Exception e) {
            //  throw internal error;
        }
        return ResponseEntity.ok().build();
    }
}
