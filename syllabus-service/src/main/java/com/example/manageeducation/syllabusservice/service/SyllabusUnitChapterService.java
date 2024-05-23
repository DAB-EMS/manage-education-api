package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.model.SyllabusUnitChapter;
import org.springframework.stereotype.Service;

@Service
public interface SyllabusUnitChapterService {
    String createSyllabusUnitChapter(SyllabusUnitChapter dto);
}
