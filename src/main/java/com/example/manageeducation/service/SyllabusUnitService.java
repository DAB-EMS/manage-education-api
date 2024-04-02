package com.example.manageeducation.service;

import com.example.manageeducation.entity.SyllabusUnit;
import org.springframework.stereotype.Service;

@Service
public interface SyllabusUnitService {
    String createSyllabusUnit(SyllabusUnit dto);
}
