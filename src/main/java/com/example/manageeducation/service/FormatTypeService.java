package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.FormatTypeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FormatTypeService {
    List<FormatTypeResponse> formatTypeResponse();
}
