package com.example.manageeducation.trainingclassservice.client;

import com.example.manageeducation.trainingclassservice.dto.TrainingProgram;
import com.example.manageeducation.trainingclassservice.dto.response.TrainingProgramResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Component
@FeignClient(name = "training-program-service", url = "${application.config.program-url}")
public interface TrainingProgramClient {

    @PostMapping("customer/training-program/{training-program-id}/duplicated")
    TrainingProgram duplicatedTrainingProgram(Principal principal, @PathVariable("training-program-id") UUID id);

    @GetMapping("customer/training-program/{training-program-id}")
    TrainingProgramResponse createTrainingProgram(@PathVariable("training-program-id") UUID id);

    @GetMapping("customer/training-program/name")
    TrainingProgram getTrainingProgram(
            @RequestParam String name,
            @RequestParam String version);

    @GetMapping("customer/training-program/{training-program-id}/id")
    Optional<TrainingProgram> findById(@PathVariable("training-program-id") UUID id);
}
