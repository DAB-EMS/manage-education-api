package com.example.manageeducation.trainingclassservice.service.impl;

import com.example.manageeducation.trainingclassservice.dto.response.FormatTypeResponse;
import com.example.manageeducation.trainingclassservice.model.FormatType;
import com.example.manageeducation.trainingclassservice.repository.FormatTypeRepository;
import com.example.manageeducation.trainingclassservice.service.FormatTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormatTypeServiceImpl implements FormatTypeService {

    @Autowired
    FormatTypeRepository formatTypeRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<FormatTypeResponse> formatTypeResponse() {
        List<FormatType> formatTypes = formatTypeRepository.findAll();
        return formatTypes
                .stream()
                .map(formatType -> modelMapper.map(formatType,FormatTypeResponse.class))
                .collect(Collectors.toList());
    }
}
