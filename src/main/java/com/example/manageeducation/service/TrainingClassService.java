package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.TrainingClassRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TrainingClassService {
    String createTrainingClass(String customerId, UUID trainingProgramId, TrainingClassRequest dto);
}
