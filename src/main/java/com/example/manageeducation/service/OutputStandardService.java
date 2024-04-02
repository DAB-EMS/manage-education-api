package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.OutputStandardRequest;
import com.example.manageeducation.dto.response.OutputStandardResponse;
import com.example.manageeducation.entity.OutputStandard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OutputStandardService {
    OutputStandard createOutputStandard(OutputStandardRequest dto);
    OutputStandard updateOutputStandard(UUID id, OutputStandardRequest dto);
    List<OutputStandardResponse> outputStandards();
}
