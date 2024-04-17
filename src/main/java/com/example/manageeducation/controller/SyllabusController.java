package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.SyllabusRequest;
import com.example.manageeducation.dto.request.SyllabusUpdateRequest;
import com.example.manageeducation.service.SyllabusService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class SyllabusController {

    @Autowired
    SyllabusService syllabusService;

    @PostMapping("/customer/syllabus")
    public ResponseEntity<?> createSyllabus(Principal principal, @RequestBody SyllabusRequest dto) {
        return ResponseEntity.ok(syllabusService.createSyllabus(principal,dto));
    }

    @PostMapping("/customer/syllabus/{syllabus-id}/duplicated")
    public ResponseEntity<?> duplicatedSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.duplicatedSyllabus(id));
    }

    @GetMapping("/syllabus/{syllabus-id}")
    public ResponseEntity<?> getSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.syllabus(id));
    }

    @GetMapping("/syllabus")
    public ResponseEntity<?> getSyllabuses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Date date) {
        return ResponseEntity.ok(syllabusService.syllabuses(search, date));
    }

    @GetMapping("/syllabuses/program-syllabus")
    public ResponseEntity<?> getSyllabusesProgram() {
        return ResponseEntity.ok(syllabusService.viewSyllabusProgram());
    }

    @DeleteMapping("/customer/syllabus/{syllabus-id}")
    public ResponseEntity<?> deleteSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.deleteSyllabus(id));
    }

    @PutMapping("/customer/syllabus/{syllabus-id}")
    public ResponseEntity<?> putSyllabus(@PathVariable("syllabus-id") UUID id, @RequestBody SyllabusUpdateRequest dto) {
        return ResponseEntity.ok(syllabusService.updateSyllabus(id,dto));
    }

    @PutMapping("/customer/syllabus/{syllabus-id}/de-active")
    public ResponseEntity<?> deActiveSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.deActive(id));
    }

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "customer/syllabus/import", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file, Principal principal) {
        try {
            return ResponseEntity.ok(syllabusService.importSyllabus(principal,file));
        } catch (Exception e) {
            //  throw internal error;
        }
        return ResponseEntity.ok().build();
    }
}
