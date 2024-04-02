package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.OutputStandardRequest;
import com.example.manageeducation.dto.response.OutputStandardResponse;
import com.example.manageeducation.entity.OutputStandard;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.OutputStandardRepository;
import com.example.manageeducation.service.OutputStandardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OutputStandardServiceImpl implements OutputStandardService {

    @Autowired
    OutputStandardRepository outputStandardRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public OutputStandard createOutputStandard(OutputStandardRequest dto) {
        return outputStandardRepository.save(modelMapper.map(dto,OutputStandard.class));
    }

    @Override
    public OutputStandard updateOutputStandard(UUID id, OutputStandardRequest dto) {
        Optional<OutputStandard> outputStandardOptional = outputStandardRepository.findById(id);
        if(outputStandardOptional.isPresent()){
            OutputStandard outputStandard = outputStandardOptional.get();
            outputStandard.setCode(dto.getCode());
            outputStandard.setName(dto.getName());
            outputStandard.setDescription(dto.getDescription());
            return outputStandardRepository.save(outputStandard);
        }else{
            throw new BadRequestException("Output standard id not found.");
        }
    }

    @Override
    public List<OutputStandardResponse> outputStandards() {
        List<OutputStandard> outputStandardResponses = outputStandardRepository.findAll();
        return outputStandardResponses.stream()
                .map(outputStandard -> modelMapper.map(outputStandard, OutputStandardResponse.class))
                .collect(Collectors.toList());
    }
}
