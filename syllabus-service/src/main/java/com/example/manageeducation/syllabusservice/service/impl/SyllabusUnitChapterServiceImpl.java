package com.example.manageeducation.syllabusservice.service.impl;

import com.example.manageeducation.entity.SyllabusUnitChapter;
import com.example.manageeducation.repository.SyllabusUnitChapterRepository;
import com.example.manageeducation.service.SyllabusUnitChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyllabusUnitChapterServiceImpl implements SyllabusUnitChapterService {

    @Autowired
    SyllabusUnitChapterRepository syllabusUnitChapterRepository;

    @Override
    public String createSyllabusUnitChapter(SyllabusUnitChapter dto) {
        syllabusUnitChapterRepository.save(dto);
        return "create successful";
    }
}
