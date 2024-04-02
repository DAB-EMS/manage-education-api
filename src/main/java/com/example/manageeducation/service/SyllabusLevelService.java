package com.example.manageeducation.service;

import com.example.manageeducation.entity.SyllabusLevel;
import org.springframework.stereotype.Service;

@Service
public interface SyllabusLevelService {
    String createSyllabusLevel(SyllabusLevel dto);
}
