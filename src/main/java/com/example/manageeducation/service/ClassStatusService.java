package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.ClassStatusResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassStatusService {
    List<ClassStatusResponse> classStatusResponse();
}
