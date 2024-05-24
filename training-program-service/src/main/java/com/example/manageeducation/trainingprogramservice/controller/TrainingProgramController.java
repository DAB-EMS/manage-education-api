package com.example.manageeducation.trainingprogramservice.controller;

import com.example.manageeducation.trainingprogramservice.dto.TrainingProgramRequest;
import com.example.manageeducation.trainingprogramservice.model.TrainingProgram;
import com.example.manageeducation.trainingprogramservice.service.TrainingProgramService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth/")
public class TrainingProgramController {

    @Autowired
    TrainingProgramService trainingProgramService;

    @PreAuthorize("hasAuthority('CREATE_TRAINING_PROGRAM')")
    @PostMapping("customer/training-program")
    public ResponseEntity<?> createTrainingProgram(Principal principal, @RequestBody TrainingProgramRequest dto) {
        return ResponseEntity.ok(trainingProgramService.createTrainingProgram(principal,dto));
    }

//    @PreAuthorize("hasAuthority('VIEW_TRAINING_PROGRAM')")
    @GetMapping("customer/training-program/{training-program-id}")
    public ResponseEntity<?> createTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.viewTrainingProgram(id));
    }

    @GetMapping("customer/training-program/name")
    public ResponseEntity<?> getTrainingProgram(
            @RequestParam String name,
            @RequestParam String version) {
        TrainingProgram trainingProgram = trainingProgramService.trainingProgramName(name, version);
        if (trainingProgram != null) {
            return ResponseEntity.ok(trainingProgram);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('VIEW_TRAINING_PROGRAM')")
    @GetMapping("customer/training-programs/classes")
    public ResponseEntity<?> listTrainingProgramClass() {
        return ResponseEntity.ok(trainingProgramService.trainingProgramAddClass());
    }

    @PreAuthorize("hasAuthority('VIEW_TRAINING_PROGRAM')")
    @GetMapping("customer/training-program")
    public ResponseEntity<?> TrainingPrograms() {
        return ResponseEntity.ok(trainingProgramService.trainingPrograms());
    }

    @PreAuthorize("hasAuthority('MODIFY_TRAINING_PROGRAM')")
    @PutMapping("customer/training-program/{training-program-id}/de-active")
    public ResponseEntity<?> inActiveTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.deActiveTrainingProgram(id));
    }

    @PreAuthorize("hasAuthority('MODIFY_TRAINING_PROGRAM')")
    @DeleteMapping("customer/training-program/{training-program-id}")
    public ResponseEntity<?> deleteTrainingProgram(@PathVariable("training-program-id") UUID id) {
        return ResponseEntity.ok(trainingProgramService.deleteTrainingProgram(id));
    }

//    @PreAuthorize("hasAuthority('CREATE_TRAINING_PROGRAM')")
    @PostMapping("customer/training-program/{training-program-id}/duplicated")
    public ResponseEntity<?> duplicatedTrainingProgram(Principal principal, @PathVariable("training-program-id") UUID id) {
        if(trainingProgramService.duplicatedTrainingProgram(principal,id)!=null){
            return ResponseEntity.ok("duplicated successful.");
        }else{
            return ResponseEntity.ok("duplicated fail.");
        }
    }

    @PreAuthorize("hasAuthority('CREATE_TRAINING_PROGRAM')")
    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "customer/training-program/import", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file, Principal principal) {
        try {
            return ResponseEntity.ok(trainingProgramService.importTrainingProgram(file,principal));
        } catch (Exception e) {
            //  throw internal error;
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CREATE_TRAINING_PROGRAM')")
    @GetMapping("customer/training-program/template/download")
    public @ResponseBody byte[] downloadXlsxTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        response.addHeader("Content-Disposition", "attachment; filename=\"TrainingProgramTemplate.xlsx\"");
        InputStream file;
        try {
            file = getClass().getResourceAsStream("/templates/TrainingProgramTemplate.xlsx");
        }catch (Exception ex) {
            throw new FileNotFoundException("File template not exist.");
        }
        return IOUtils.toByteArray(file);
    }
}
