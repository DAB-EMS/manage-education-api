package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.SyllabusRequest;
import com.example.manageeducation.entity.Syllabus;
import org.springframework.stereotype.Service;

@Service
public interface SyllabusService {
    String createSyllabus(String id, SyllabusRequest dto);
}
