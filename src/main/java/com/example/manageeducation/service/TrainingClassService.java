package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.dto.response.TrainingClassesResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public interface TrainingClassService {
    String createTrainingClass(Principal principal, UUID trainingProgramId, TrainingClassRequest dto);
    List<TrainingClassesResponse> TrainingClassesResponses();
    String deleteTrainingClass(UUID id);
    String duplicated(Principal principal, UUID id);
}
