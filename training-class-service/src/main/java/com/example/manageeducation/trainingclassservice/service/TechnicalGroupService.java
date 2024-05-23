package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.TechnicalGroupResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TechnicalGroupService {
    List<TechnicalGroupResponse> technicalGroupResponse();
}
