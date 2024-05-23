package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.request.OutputStandardRequest;
import com.example.manageeducation.syllabusservice.dto.response.OutputStandardResponse;
import com.example.manageeducation.syllabusservice.model.OutputStandard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OutputStandardService {
    OutputStandard createOutputStandard(OutputStandardRequest dto);
    OutputStandard updateOutputStandard(UUID id, OutputStandardRequest dto);
    List<OutputStandardResponse> outputStandards();
}
