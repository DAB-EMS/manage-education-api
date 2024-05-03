package com.example.manageeducation.controller;

import com.example.manageeducation.Utils.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
public class testController {

    @GetMapping("/information/config")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAdminConfigurationInfo() throws JsonProcessingException {
        return ResponseEntity.ok("Good");
    }

    @Autowired
    SecurityUtil securityUtil;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Principal principal) {
        return ResponseEntity.ok(securityUtil.getLoginUser(principal));
    }


    @GetMapping("/userd")
    @PreAuthorize("hasAuthority('FULL_ACCESS_CLASS')")
    public ResponseEntity<?> getUdsedasr() {
        return ResponseEntity.ok("oh nha");
    }

    @GetMapping("/userdsd")
    @PreAuthorize("hasAuthority('VIEW_CLASS')")
    public ResponseEntity<?> getUsedasr() {
        return ResponseEntity.ok("oh nha");
    }

    @GetMapping("/usedasdrdsd")
    @PreAuthorize("hasAuthority('VIEW_SYLLABUS')")
    public ResponseEntity<?> getUssedasr() {
        return ResponseEntity.ok("oh nha");
    }

    @GetMapping("/usedasdasrdsd")
    @PreAuthorize("hasAuthority('MODIFY_SYLLABUS')")
    public ResponseEntity<?> getUseddasr() {
        return ResponseEntity.ok("oh nha");
    }

}
