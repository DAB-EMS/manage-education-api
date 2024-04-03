package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.SyllabusUnitAddRequest;
import com.example.manageeducation.dto.request.SyllabusUnitRequest;
import com.example.manageeducation.dto.response.SyllabusResponse;
import com.example.manageeducation.dto.response.SyllabusUnitResponse;
import com.example.manageeducation.entity.SyllabusUnit;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SyllabusUnitService {
    SyllabusUnitResponse createSyllabusUnit(UUID syllabusId, UUID syllabusDayId, SyllabusUnitAddRequest dto);
    SyllabusUnitResponse updateSyllabusUnit(UUID unitId, SyllabusUnitAddRequest dto);
}
