package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.repository.SyllabusUnitRepository;
import com.example.manageeducation.service.SyllabusUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyllabusUnitServiceImpl implements SyllabusUnitService {

    @Autowired
    SyllabusUnitRepository syllabusUnitRepository;
    @Override
    public String createSyllabusUnit(SyllabusUnit dto) {
        syllabusUnitRepository.save(dto);
        return "create success";
    }
}
