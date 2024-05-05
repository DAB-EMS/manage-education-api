package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.DeliveryTypeRequest;
import com.example.manageeducation.service.DeliveryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/syllabus/syllabus-day/syllabus-unit/unit-chapter")
public class DeliveryTypeController {

    @Autowired
    DeliveryTypeService deliveryTypeService;

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @PostMapping("/delivery-type")
    public ResponseEntity<?> createDeliveryType(@RequestBody DeliveryTypeRequest dto) {
        return ResponseEntity.ok(deliveryTypeService.createDeliveryType(dto));
    }

    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    @PutMapping("/delivery-type/{delivery-type-id}")
    public ResponseEntity<?> updateDeliveryType(@PathVariable("delivery-type-id") UUID id, @RequestBody DeliveryTypeRequest dto) {
        return ResponseEntity.ok(deliveryTypeService.updateDeliveryType(id,dto));
    }

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/delivery-types")
    public ResponseEntity<?> deliveryTypes() {
        return ResponseEntity.ok(deliveryTypeService.deliveryTypes());
    }

}
