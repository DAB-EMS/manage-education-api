package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.SyllabusDayAddRequest;
import com.example.manageeducation.dto.request.SyllabusDayRequest;
import com.example.manageeducation.dto.response.SyllabusDayResponse;
import com.example.manageeducation.entity.SyllabusDay;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SyllabusDayService {
    SyllabusDay createSyllabusDay(UUID id, SyllabusDayAddRequest dto);
    String deleteSyllabusDay(UUID id);
    List<SyllabusDayResponse> syllabusDays(UUID id);
}
