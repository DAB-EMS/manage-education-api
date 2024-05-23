package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.request.SyllabusUnitAddRequest;
import com.example.manageeducation.syllabusservice.dto.response.SyllabusUnitResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SyllabusUnitService {
    SyllabusUnitResponse createSyllabusUnit(UUID syllabusId, UUID syllabusDayId, SyllabusUnitAddRequest dto);
    SyllabusUnitResponse updateSyllabusUnit(UUID unitId, SyllabusUnitAddRequest dto);
}
