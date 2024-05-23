package com.example.manageeducation.trainingclassservice.service;

import com.example.manageeducation.trainingclassservice.dto.response.FsuTrainingClassResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FsuService {
    List<FsuTrainingClassResponse> listFsu();
}
