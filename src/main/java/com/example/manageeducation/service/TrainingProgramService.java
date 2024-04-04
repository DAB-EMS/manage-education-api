package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.TrainingProgramRequest;
import org.springframework.stereotype.Service;

@Service
public interface TrainingProgramService {
    String createTrainingProgram(String customerId, TrainingProgramRequest dto);
}
