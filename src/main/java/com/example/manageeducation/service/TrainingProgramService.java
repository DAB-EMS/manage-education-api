package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.TrainingProgramImportRequest;
import com.example.manageeducation.dto.request.TrainingProgramRequest;
import com.example.manageeducation.dto.response.TrainingProgramResponse;
import com.example.manageeducation.dto.response.TrainingProgramsResponse;
import com.example.manageeducation.entity.TrainingProgram;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public interface TrainingProgramService {
    String createTrainingProgram(Principal principal, TrainingProgramRequest dto);
    TrainingProgramResponse viewTrainingProgram(UUID id);
    String deActiveTrainingProgram(UUID id);
    String deleteTrainingProgram(UUID id);
    TrainingProgram duplicatedTrainingProgram(Principal principal, UUID id);
    List<TrainingProgramsResponse> trainingPrograms();
    String importTrainingProgram(MultipartFile file, Principal principal);
}
