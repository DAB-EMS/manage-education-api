package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.ProgramContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgramContentService {
    List<ProgramContentResponse> programContentResponses();
}
