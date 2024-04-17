package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.SyllabusRequest;
import com.example.manageeducation.dto.request.SyllabusUpdateRequest;
import com.example.manageeducation.dto.response.SyllabusResponse;
import com.example.manageeducation.dto.response.SyllabusViewProgramResponse;
import com.example.manageeducation.dto.response.ViewSyllabusResponse;
import com.example.manageeducation.entity.Syllabus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public interface SyllabusService {
    String createSyllabus(Principal principal, SyllabusRequest dto);
    String importSyllabus(Principal principal, MultipartFile file);
    SyllabusResponse syllabus(UUID id);
    List<ViewSyllabusResponse> syllabuses(String search, Date date);
    String duplicatedSyllabus(UUID id);
    String deleteSyllabus(UUID id);
    Syllabus updateSyllabus(UUID id, SyllabusUpdateRequest dto);
    String deActive(UUID id);
    List<SyllabusViewProgramResponse> viewSyllabusProgram();
}
