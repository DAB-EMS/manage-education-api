package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.AttendLevelResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendLevelService {
    List<AttendLevelResponse> attendLevels();
}
