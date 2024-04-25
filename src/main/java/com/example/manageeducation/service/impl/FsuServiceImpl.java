package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.response.ContactPointResponse;
import com.example.manageeducation.dto.response.FsuTrainingClassResponse;
import com.example.manageeducation.entity.ContactPoint;
import com.example.manageeducation.entity.Fsu;
import com.example.manageeducation.repository.FsuRepository;
import com.example.manageeducation.service.FsuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
