package com.example.manageeducation.syllabusservice.controller;

import com.example.manageeducation.syllabusservice.dto.RequestForListOfSyllabus;
import com.example.manageeducation.syllabusservice.dto.request.SyllabusRequest;
import com.example.manageeducation.syllabusservice.dto.request.SyllabusUpdateRequest;
import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import com.example.manageeducation.syllabusservice.model.ResponseObject;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import com.example.manageeducation.syllabusservice.service.SyllabusService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class SyllabusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyllabusController.class);

    @Autowired
    SyllabusService syllabusService;

    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
    @PostMapping("/customer/syllabus")
    public ResponseEntity<?> createSyllabus(Principal principal, @RequestBody SyllabusRequest dto) {
        return ResponseEntity.ok(syllabusService.createSyllabus(principal,dto));
    }

    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
    @PostMapping("/customer/syllabus/{syllabus-id}/duplicated")
    public ResponseEntity<?> duplicatedSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.duplicatedSyllabus(id));
    }

//    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/syllabus/{syllabus-id}")
    public ResponseEntity<?> getSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.syllabus(id));
    }

    @GetMapping("/syllabus/check")
    public ResponseEntity<List<Syllabus>> checkCondition(
            @RequestParam String name,
            @RequestParam String code,
            @RequestParam String version,
            @RequestParam SyllabusStatus status) {
        List<Syllabus> syllabusList = syllabusService.checkCondition(name, code, version, status);
        return ResponseEntity.ok(syllabusList);
    }

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/syllabus")
    public ResponseEntity<?> getSyllabuses() {
        return ResponseEntity.ok(syllabusService.syllabuses());
    }

    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    @GetMapping("/syllabuses/program-syllabus")
    public ResponseEntity<?> getSyllabusesProgram() {
        return ResponseEntity.ok(syllabusService.viewSyllabusProgram());
    }

    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    @DeleteMapping("/customer/syllabus/{syllabus-id}")
    public ResponseEntity<?> deleteSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.deleteSyllabus(id));
    }

    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    @PutMapping("/customer/syllabus/{syllabus-id}")
    public ResponseEntity<?> putSyllabus(@PathVariable("syllabus-id") UUID id, @RequestBody SyllabusUpdateRequest dto) {
        return ResponseEntity.ok(syllabusService.updateSyllabus(id,dto));
    }

    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    @PutMapping("/customer/syllabus/{syllabus-id}/de-active")
    public ResponseEntity<?> deActiveSyllabus(@PathVariable("syllabus-id") UUID id) {
        return ResponseEntity.ok(syllabusService.deActive(id));
    }


    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
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

    @PreAuthorize("hasAuthority('CREATE_SYLLABUS')")
    @GetMapping("/customer/syllabus/template/download")
    public @ResponseBody byte[] downloadXlsxTemplate(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/xlsx");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"SyllabusTemplate.zip\"");
        InputStream file;
        try {
            file = getClass().getResourceAsStream("/templates/SyllabusTemplate.zip");
        } catch (Exception e) {
            throw new FileNotFoundException("File template not exist.");
        }
        return IOUtils.toByteArray(file);
    }

    @GetMapping("/customer/syllabus/keywords")
    @Operation(summary = "get all customers ")
    public ResponseEntity<ResponseObject> searchCustomers(
            @RequestParam(name = "keyword", defaultValue = "") String[] keywords,
            @RequestParam(name = "start", defaultValue = "") String start,
            @RequestParam(name = "end", defaultValue = "") String end,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false)
            @Parameter(name = "sortBy", description = "name or age or address") String sortBy,
            @RequestParam(value = "sortType", required = false)
            @Parameter(name = "sortType", description = "ASC or DESC") String sortType) {
        RequestForListOfSyllabus request = new RequestForListOfSyllabus(keywords, start, end, page, size, sortBy, sortType);
        LOGGER.info("Start method List of Customers in SpringDataController");
        return syllabusService.getAllSyllabus(request);
    }
}
