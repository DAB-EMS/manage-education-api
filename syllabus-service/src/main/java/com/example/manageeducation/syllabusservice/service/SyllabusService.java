package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.request.SyllabusRequest;
import com.example.manageeducation.syllabusservice.dto.request.SyllabusUpdateRequest;
import com.example.manageeducation.syllabusservice.dto.response.SyllabusResponse;
import com.example.manageeducation.syllabusservice.dto.response.SyllabusViewProgramResponse;
import com.example.manageeducation.syllabusservice.dto.response.ViewSyllabusResponse;
import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public interface SyllabusService {
    String createSyllabus(Principal principal, SyllabusRequest dto);
    String importSyllabus(Principal principal, MultipartFile file);
    SyllabusResponse syllabus(UUID id);
    List<ViewSyllabusResponse> syllabuses();
    String duplicatedSyllabus(UUID id);
    String deleteSyllabus(UUID id);
    Syllabus updateSyllabus(UUID id, SyllabusUpdateRequest dto);
    String deActive(UUID id);
    List<SyllabusViewProgramResponse> viewSyllabusProgram();
    List<Syllabus> checkCondition(String name, String code, String version, SyllabusStatus status);
}
