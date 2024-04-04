package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.TrainingClassResponse;
import org.springframework.stereotype.Service;

@Service
public interface TrainingClassService {
    TrainingClassResponse viewTrainingClass();
}
