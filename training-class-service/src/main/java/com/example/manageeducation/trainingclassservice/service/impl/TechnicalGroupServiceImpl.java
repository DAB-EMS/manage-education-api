package com.example.manageeducation.trainingclassservice.service.impl;

import com.example.manageeducation.trainingclassservice.dto.response.TechnicalGroupResponse;
import com.example.manageeducation.trainingclassservice.model.TechnicalGroup;
import com.example.manageeducation.trainingclassservice.repository.TechnicalGroupRepository;
import com.example.manageeducation.trainingclassservice.service.TechnicalGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicalGroupServiceImpl implements TechnicalGroupService {

    @Autowired
    TechnicalGroupRepository technicalGroupRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<TechnicalGroupResponse> technicalGroupResponse() {
        List<TechnicalGroup> technicalGroups = technicalGroupRepository.findAll();
        return technicalGroups
                .stream()
                .map(technicalGroup -> modelMapper.map(technicalGroup,TechnicalGroupResponse.class))
                .collect(Collectors.toList());
    }
}
