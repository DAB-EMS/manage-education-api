package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.ClassStatusResponse;
import com.example.manageeducation.entity.ClassStatus;
import com.example.manageeducation.repository.ClassStatusRepository;
import com.example.manageeducation.service.ClassStatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassStatusServiceImpl implements ClassStatusService {

    @Autowired
    ClassStatusRepository classStatusRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<ClassStatusResponse> classStatusResponse() {
        List<ClassStatus> classStatuses = classStatusRepository.findAll();
        return classStatuses
                .stream()
                .map(classStatus -> modelMapper.map(classStatus,ClassStatusResponse.class))
                .collect(Collectors.toList());
    }
}
