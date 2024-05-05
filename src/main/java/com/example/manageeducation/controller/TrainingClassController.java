package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.DataExcelForTrainingClass;
import com.example.manageeducation.dto.request.TrainingClassRequest;
import com.example.manageeducation.service.TrainingClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingClassController {

    @Autowired
    TrainingClassService trainingClassService;

    @PreAuthorize("hasAuthority('CREATE_CLASS')")
    @PostMapping("customer/training-program/{training-program-id}/training-class")
    public ResponseEntity<?> createTrainingProgram(Principal principal, @PathVariable("training-program-id") UUID id, @RequestBody TrainingClassRequest dto) {
        return ResponseEntity.ok(trainingClassService.createTrainingClass(principal,id,dto));
    }

    @PreAuthorize("hasAuthority('CREATE_CLASS')")
    @PostMapping("customer/training-program/training-class/{training-class-id}/duplicated")
    public ResponseEntity<?> createTrainingProgram(Principal principal, @PathVariable("training-class-id") UUID id) {
        return ResponseEntity.ok(trainingClassService.duplicated(principal,id));
    }

    @PreAuthorize("hasAuthority('MODIFY_CLASS')")
    @DeleteMapping("customer/training-program/training-class/{training-class-id}")
    public ResponseEntity<?> deleteTrainingProgram(@PathVariable("training-class-id") UUID id) {
        return ResponseEntity.ok(trainingClassService.deleteTrainingClass(id));
    }

    @PreAuthorize("hasAuthority('VIEW_CLASS')")
    @GetMapping("customer/training-program/training-classes")
    public ResponseEntity<?> trainingProgramList() {
        return ResponseEntity.ok(trainingClassService.TrainingClassesResponses());
    }

    @PreAuthorize("hasAuthority('VIEW_CLASS')")
    @GetMapping("customer/training-program/training-classes/{training-class-id}")
    public ResponseEntity<?> trainingProgramView(@PathVariable("training-class-id") UUID id) {
        return ResponseEntity.ok(trainingClassService.viewTrainingClass(id));
    }

    @PreAuthorize("hasAuthority('CREATE_CLASS')")
    @PostMapping(value = "customer/training-program/training-class/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importTrainingClass(Principal principal, @RequestPart(required = true) MultipartFile file) {
        List<DataExcelForTrainingClass> list = trainingClassService.readDataFromExcel(principal,file);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAuthority('CREATE_CLASS')")
    @GetMapping("customer/training-program/training-class/template/download")
    public @ResponseBody byte[] downloadXlsxTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        response.addHeader("Content-Disposition", "attachment; filename=\"ClassTemplate.zip\"");
        InputStream file;
        try {
            file = getClass().getResourceAsStream("/templates/ClassTemplate.zip");
        }catch (Exception ex) {
            throw new FileNotFoundException("File template not exist.");
        }
        return IOUtils.toByteArray(file);
    }
}
