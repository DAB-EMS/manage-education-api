package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.DeliveryTypeRequest;
import com.example.manageeducation.dto.response.DeliveryTypeResponse;
import com.example.manageeducation.entity.DeliveryType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DeliveryTypeService {
    DeliveryType createDeliveryType(DeliveryTypeRequest dto);
    DeliveryType updateDeliveryType(UUID id, DeliveryTypeRequest dto);
    List<DeliveryTypeResponse> deliveryTypes();
}
