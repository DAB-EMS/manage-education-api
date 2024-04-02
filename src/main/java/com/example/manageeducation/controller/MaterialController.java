package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.MaterialRequest;
import com.example.manageeducation.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @DeleteMapping("/syllabus/syllabus-day/syllabus-unit/unit-chapter/material/{material-id}")
    public ResponseEntity<?> deleteDeliveryType(@PathVariable("material-id") UUID id) {
        return ResponseEntity.ok(materialService.deleteMaterial(id));
    }

    @PutMapping("/customer/{customer-id}/syllabus/syllabus-day/syllabus-unit/unit-chapter/material/{material-id}")
    public ResponseEntity<?> updateDeliveryType(@PathVariable("customer-id") String customerId, @PathVariable("material-id") UUID materialId, @RequestBody MaterialRequest dto) {
        return ResponseEntity.ok(materialService.updateMaterial(customerId,materialId,dto));
    }

    @PostMapping("/customer/{customer-id}/syllabus/syllabus-day/syllabus-unit/unit-chapter/{unit-chapter-id}/material")
    public ResponseEntity<?> postDeliveryType(@PathVariable("customer-id") String customerId, @PathVariable("unit-chapter-id") UUID chapterId, @RequestBody MaterialRequest dto) {
        return ResponseEntity.ok(materialService.createMaterial(customerId,chapterId,dto));
    }

    @GetMapping("/syllabus/syllabus-day/syllabus-unit/unit-chapter/{unit-chapter-id}/materials")
    public ResponseEntity<?> deliveryTypes(@PathVariable("unit-chapter-id") UUID id) {
        return ResponseEntity.ok(materialService.materials(id));
    }
}
