package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.ClassLocationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassLocationService {
    List<ClassLocationResponse> classLocationResponses();
}
