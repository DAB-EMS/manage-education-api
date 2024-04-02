package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.SyllabusLevelRequest;
import com.example.manageeducation.dto.response.SyllabusLevelResponse;
import com.example.manageeducation.entity.SyllabusLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SyllabusLevelService {
    String createSyllabusLevel(SyllabusLevelRequest dto);

    SyllabusLevelResponse updateSyllabusLevel(UUID id, SyllabusLevelRequest dto);

    List<SyllabusLevelResponse> syllabusLevelResponse();
}
