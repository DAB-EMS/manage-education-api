package com.example.manageeducation.trainingprogramservice.service;

import com.example.manageeducation.trainingprogramservice.dto.TrainingProgramAddClassRequest;
import com.example.manageeducation.trainingprogramservice.dto.TrainingProgramRequest;
import com.example.manageeducation.trainingprogramservice.dto.TrainingProgramResponse;
import com.example.manageeducation.trainingprogramservice.dto.TrainingProgramsResponse;
import com.example.manageeducation.trainingprogramservice.model.TrainingProgram;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    List<TrainingProgramAddClassRequest> trainingProgramAddClass();
    TrainingProgram trainingProgramName(String name,String version);
}
