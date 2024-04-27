package com.example.manageeducation.service;

import com.example.manageeducation.dto.response.TechnicalGroupResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TechnicalGroupService {
    List<TechnicalGroupResponse> technicalGroupResponse();
}
