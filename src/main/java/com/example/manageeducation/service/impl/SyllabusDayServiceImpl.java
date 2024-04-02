package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.repository.SyllabusDayRepository;
import com.example.manageeducation.service.SyllabusDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyllabusDayServiceImpl implements SyllabusDayService {

    @Autowired
    SyllabusDayRepository syllabusDayRepository;

    @Override
    public String createSyllabusDay(SyllabusDay dto) {
        syllabusDayRepository.save(dto);
        return "create successful";
    }
}
