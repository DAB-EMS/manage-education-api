package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.FormatTypeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FormatTypeService {
    List<FormatTypeResponse> formatTypeResponse();
}
