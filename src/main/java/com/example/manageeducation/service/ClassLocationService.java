package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.ClassLocationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassLocationService {
    List<ClassLocationResponse> classLocationResponses();
}
