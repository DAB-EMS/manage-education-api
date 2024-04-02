package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.repository.SyllabusRepository;
import com.example.manageeducation.service.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusRepository syllabusRepository;

    @Override
    public String createSyllabus(Syllabus dto) {
        syllabusRepository.save(dto);
        return "create successful";
    }
}
