package com.example.manageeducation.trainingclassservice.service.impl;

import com.example.manageeducation.trainingclassservice.dto.response.AttendLevelResponse;
import com.example.manageeducation.trainingclassservice.model.AttendLevel;
import com.example.manageeducation.trainingclassservice.repository.AttendLevelRepository;
import com.example.manageeducation.trainingclassservice.service.AttendLevelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendLevelServiceImpl implements AttendLevelService {

    @Autowired
    AttendLevelRepository attendLevelRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<AttendLevelResponse> attendLevels() {
        List<AttendLevel> attendLevels = attendLevelRepository.findAll();
        return attendLevels
                .stream()
                .map(attendLevel -> modelMapper.map(attendLevel, AttendLevelResponse.class))
                .collect(Collectors.toList());
    }
}
