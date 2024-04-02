package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.SyllabusLevel;
import com.example.manageeducation.repository.SyllabusLevelRepository;
import com.example.manageeducation.service.SyllabusLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyllabusLevelServiceImpl implements SyllabusLevelService {

    @Autowired
    SyllabusLevelRepository syllabusLevelRepository;

    @Override
    public String createSyllabusLevel(SyllabusLevel dto) {
        syllabusLevelRepository.save(dto);
        return "create successful";
    }
}
