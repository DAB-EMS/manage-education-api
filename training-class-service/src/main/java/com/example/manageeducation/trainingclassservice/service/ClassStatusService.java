package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.ClassStatusResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassStatusService {
    List<ClassStatusResponse> classStatusResponse();
}
