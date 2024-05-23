package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.request.SyllabusLevelRequest;
import com.example.manageeducation.syllabusservice.dto.response.SyllabusLevelResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SyllabusLevelService {
    String createSyllabusLevel(SyllabusLevelRequest dto);

    SyllabusLevelResponse updateSyllabusLevel(UUID id, SyllabusLevelRequest dto);

    List<SyllabusLevelResponse> syllabusLevelResponse();
}
