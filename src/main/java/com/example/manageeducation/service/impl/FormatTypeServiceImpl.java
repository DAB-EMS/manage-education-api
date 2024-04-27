package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.FormatTypeResponse;
import com.example.manageeducation.entity.FormatType;
import com.example.manageeducation.repository.FormatTypeRepository;
import com.example.manageeducation.service.FormatTypeService;
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
