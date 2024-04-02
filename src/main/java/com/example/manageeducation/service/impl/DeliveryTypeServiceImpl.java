package com.example.manageeducation.service.impl;

import com.example.manageeducation.dto.request.DeliveryTypeRequest;
import com.example.manageeducation.dto.response.DeliveryTypeResponse;
import com.example.manageeducation.dto.response.OutputStandardResponse;
import com.example.manageeducation.entity.DeliveryType;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.DeliveryTypeRepository;
import com.example.manageeducation.service.DeliveryTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeliveryTypeServiceImpl implements DeliveryTypeService {

    @Autowired
    DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public DeliveryType createDeliveryType(DeliveryTypeRequest dto) {
        return deliveryTypeRepository.save(modelMapper.map(dto,DeliveryType.class));
    }

    @Override
    public DeliveryType updateDeliveryType(UUID id, DeliveryTypeRequest dto) {
        Optional<DeliveryType> deliveryTypeOptional = deliveryTypeRepository.findById(id);
        if(deliveryTypeOptional.isPresent()){
            DeliveryType deliveryType = deliveryTypeOptional.get();
            deliveryType.setName(deliveryType.getName());
            deliveryType.setDescription(dto.getDescription());
            return deliveryTypeRepository.save(deliveryType);
        }else{
            throw new BadRequestException("Delivery type id not found.");
        }
    }

    @Override
    public List<DeliveryTypeResponse> deliveryTypes() {
        List<DeliveryType> deliveryTypes = deliveryTypeRepository.findAll();
        return deliveryTypes.stream()
                .map(deliveryType -> modelMapper.map(deliveryType, DeliveryTypeResponse.class))
                .collect(Collectors.toList());
    }
}
