package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.request.RequestForListOfTrainingClass;
import com.example.manageeducation.trainingclassservice.dto.response.DataExcelForTrainingClass;
import com.example.manageeducation.trainingclassservice.dto.request.TrainingClassRequest;
import com.example.manageeducation.trainingclassservice.dto.response.TrainingClassViewResponse;
import com.example.manageeducation.trainingclassservice.dto.response.TrainingClassesResponse;
import com.example.manageeducation.trainingclassservice.model.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public interface TrainingClassService {
    String createTrainingClass(Principal principal, UUID trainingProgramId, TrainingClassRequest dto);
    List<TrainingClassesResponse> TrainingClassesResponses();
    String deleteTrainingClass(UUID id);
    String duplicated(Principal principal, UUID id);
    TrainingClassViewResponse viewTrainingClass(UUID id);
    List<DataExcelForTrainingClass> readDataFromExcel(Principal principal, MultipartFile file);
    ResponseEntity<ResponseObject> getAllTrainingClasses(RequestForListOfTrainingClass request);
}
