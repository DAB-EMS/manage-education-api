package com.example.manageeducation.trainingclassservice.service.impl;

import com.example.manageeducation.trainingclassservice.dto.response.ContactPointResponse;
import com.example.manageeducation.trainingclassservice.dto.response.FsuTrainingClassResponse;
import com.example.manageeducation.trainingclassservice.model.ContactPoint;
import com.example.manageeducation.trainingclassservice.model.Fsu;
import com.example.manageeducation.trainingclassservice.repository.FsuRepository;
import com.example.manageeducation.trainingclassservice.service.FsuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FsuServiceImpl implements FsuService {

    @Autowired
    FsuRepository fsuRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<FsuTrainingClassResponse> listFsu() {
        List<FsuTrainingClassResponse> fsuTrainingClassResponses = new ArrayList<>();
        List<Fsu> fsus = fsuRepository.findAll();
        for(Fsu fsu:fsus){
            FsuTrainingClassResponse fsuTrainingClassResponse = new FsuTrainingClassResponse();
            fsuTrainingClassResponse.setId(fsu.getId());
            fsuTrainingClassResponse.setName(fsu.getName());
            List<ContactPointResponse> contactPointResponses = new ArrayList<>();
            for(ContactPoint contactPoint: fsu.getContactPointList()){
                contactPointResponses.add(modelMapper.map(contactPoint, ContactPointResponse.class));
            }
            fsuTrainingClassResponse.setContactPoints(contactPointResponses);
            fsuTrainingClassResponses.add(fsuTrainingClassResponse);
        }
        return fsuTrainingClassResponses;
    }

}
