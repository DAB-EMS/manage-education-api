package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.ClassLocationResponse;
import com.example.manageeducation.entity.ClassLocation;
import com.example.manageeducation.repository.ClassLocationRepository;
import com.example.manageeducation.service.ClassLocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassLocationServiceImpl implements ClassLocationService {
    @Autowired
    ClassLocationRepository classLocationRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ClassLocationResponse> classLocationResponses() {
        List<ClassLocation> classLocations = classLocationRepository.findAll();
        return classLocations
                .stream()
                .map(classLocation -> modelMapper.map(classLocation, ClassLocationResponse.class))
                .collect(Collectors.toList());
    }
}
