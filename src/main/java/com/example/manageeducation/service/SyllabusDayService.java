package com.example.manageeducation.service;

import com.example.manageeducation.entity.SyllabusDay;
import org.springframework.stereotype.Service;

@Service
public interface SyllabusDayService {
    String createSyllabusDay(SyllabusDay dto);
}
