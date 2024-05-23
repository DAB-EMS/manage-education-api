package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.AttendLevelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendLevelService {
    List<AttendLevelResponse> attendLevels();
}
