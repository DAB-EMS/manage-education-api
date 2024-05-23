package com.example.manageeducation.trainingclassservice.service.impl;

import com.example.manageeducation.trainingclassservice.dto.response.ProgramContentResponse;
import com.example.manageeducation.trainingclassservice.model.ProgramContent;
import com.example.manageeducation.trainingclassservice.repository.ProgramContentRepository;
import com.example.manageeducation.trainingclassservice.service.ProgramContentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramContentServiceImpl implements ProgramContentService {

    @Autowired
    ProgramContentRepository programContentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ProgramContentResponse> programContentResponses() {
        List<ProgramContent> programContents = programContentRepository.findAll();
        return programContents
                .stream()
                .map(programContent -> modelMapper.map(programContent,ProgramContentResponse.class))
                .collect(Collectors.toList());
    }
}
