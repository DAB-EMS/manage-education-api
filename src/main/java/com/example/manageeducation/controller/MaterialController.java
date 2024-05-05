package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.MaterialRequest;
import com.example.manageeducation.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @PreAuthorize("hasAuthority('MODIFY_LEARNING_MATERIAL')")
    @DeleteMapping("/syllabus/syllabus-day/syllabus-unit/unit-chapter/material/{material-id}")
    public ResponseEntity<?> deleteDeliveryType(@PathVariable("material-id") UUID id) {
        return ResponseEntity.ok(materialService.deleteMaterial(id));
    }

    @PreAuthorize("hasAuthority('MODIFY_LEARNING_MATERIAL')")
    @PutMapping("/customer/syllabus/syllabus-day/syllabus-unit/unit-chapter/material/{material-id}")
    public ResponseEntity<?> updateDeliveryType(Principal principal, @PathVariable("material-id") UUID materialId, @RequestBody MaterialRequest dto) {
        return ResponseEntity.ok(materialService.updateMaterial(principal,materialId,dto));
    }

    @PreAuthorize("hasAuthority('CREATE_LEARNING_MATERIAL')")
    @PostMapping("/customer/syllabus/syllabus-day/syllabus-unit/unit-chapter/{unit-chapter-id}/material")
    public ResponseEntity<?> postDeliveryType(Principal principal, @PathVariable("unit-chapter-id") UUID chapterId, @RequestBody MaterialRequest dto) {
        return ResponseEntity.ok(materialService.createMaterial(principal,chapterId,dto));
    }

    @PreAuthorize("hasAuthority('VIEW_LEARNING_MATERIAL')")
    @GetMapping("/syllabus/syllabus-day/syllabus-unit/unit-chapter/{unit-chapter-id}/materials")
    public ResponseEntity<?> deliveryTypes(@PathVariable("unit-chapter-id") UUID id) {
        return ResponseEntity.ok(materialService.materials(id));
    }

    @PreAuthorize("hasAuthority('VIEW_LEARNING_MATERIAL')")
    @GetMapping("/syllabus/syllabus-day/syllabus-unit/unit-chapter/materials")
    public ResponseEntity<?> materialFull() {
        return ResponseEntity.ok(materialService.materialFull());
    }
}
