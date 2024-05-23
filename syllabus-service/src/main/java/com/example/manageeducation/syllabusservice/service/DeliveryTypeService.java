package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.request.DeliveryTypeRequest;
import com.example.manageeducation.syllabusservice.dto.response.DeliveryTypeResponse;
import com.example.manageeducation.syllabusservice.model.DeliveryType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DeliveryTypeService {
    DeliveryType createDeliveryType(DeliveryTypeRequest dto);
    DeliveryType updateDeliveryType(UUID id, DeliveryTypeRequest dto);
    List<DeliveryTypeResponse> deliveryTypes();
}
