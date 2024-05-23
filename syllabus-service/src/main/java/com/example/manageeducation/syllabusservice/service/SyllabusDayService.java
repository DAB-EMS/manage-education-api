package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.request.SyllabusDayAddRequest;
import com.example.manageeducation.syllabusservice.dto.response.SyllabusDayResponse;
import com.example.manageeducation.syllabusservice.model.SyllabusDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SyllabusDayService {
    SyllabusDay createSyllabusDay(UUID id, SyllabusDayAddRequest dto);
    String deleteSyllabusDay(UUID id);
    List<SyllabusDayResponse> syllabusDays(UUID id);
}
