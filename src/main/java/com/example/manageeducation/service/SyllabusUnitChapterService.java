package com.example.manageeducation.service;

import com.example.manageeducation.entity.SyllabusUnitChapter;
import org.springframework.stereotype.Service;

@Service
public interface SyllabusUnitChapterService {
    String createSyllabusUnitChapter(SyllabusUnitChapter dto);
}
