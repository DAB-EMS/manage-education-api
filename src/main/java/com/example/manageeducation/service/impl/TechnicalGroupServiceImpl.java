package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.TechnicalGroupResponse;
import com.example.manageeducation.entity.TechnicalGroup;
import com.example.manageeducation.repository.TechnicalGroupRepository;
import com.example.manageeducation.service.TechnicalGroupService;
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
