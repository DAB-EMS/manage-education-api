package com.example.manageeducation.trainingprogramservice.client;

import com.example.manageeducation.trainingprogramservice.dto.Syllabus;
import com.example.manageeducation.trainingprogramservice.enums.SyllabusStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@FeignClient(name = "user-service", url = "${application.config.program-url}")
public interface SyllabusClient {
    @GetMapping("/syllabus/{syllabus-id}")
    Optional<Syllabus> getSyllabus(@PathVariable("syllabus-id") UUID id);

    @GetMapping("/syllabus/check")
    List<Syllabus> checkCondition(
            @RequestParam String name,
            @RequestParam String code,
            @RequestParam String version,
            @RequestParam SyllabusStatus status);
}
