package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.TrainingClassResponse;
import com.example.manageeducation.entity.TrainingClass;
import com.example.manageeducation.repository.TrainingClassRepository;
import com.example.manageeducation.service.TrainingClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingClassServiceImpl implements TrainingClassService {

    @Autowired
    TrainingClassRepository trainingClassRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public TrainingClassResponse viewTrainingClass() {
        List<TrainingClass> trainingClassList = trainingClassRepository.findAll();
        return modelMapper.map(trainingClassList,TrainingClassResponse.class);
    }
}
