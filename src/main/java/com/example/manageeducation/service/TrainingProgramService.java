package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.TrainingProgramRequest;
import com.example.manageeducation.dto.response.TrainingProgramResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TrainingProgramService {
    String createTrainingProgram(String customerId, TrainingProgramRequest dto);
    TrainingProgramResponse viewTrainingProgram(UUID id);
    String deActiveTrainingProgram(UUID id);
    String duplicatedTrainingProgram(String customerId, UUID id);
}
