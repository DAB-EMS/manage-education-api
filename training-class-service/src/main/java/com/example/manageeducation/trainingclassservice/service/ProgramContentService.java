package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.ProgramContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgramContentService {
    List<ProgramContentResponse> programContentResponses();
}
