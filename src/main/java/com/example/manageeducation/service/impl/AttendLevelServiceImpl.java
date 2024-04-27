package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.AttendLevelResponse;
import com.example.manageeducation.entity.AttendLevel;
import com.example.manageeducation.repository.AttendLevelRepository;
import com.example.manageeducation.service.AttendLevelService;
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
