package com.example.manageeducation.userservice.controller;

import com.example.manageeducation.userservice.dto.RolePermissionsRequest;
import com.example.manageeducation.userservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/all-role")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.roles());
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/roles/permissions")
    public ResponseEntity<?> getRolePermission() {
        return ResponseEntity.ok(roleService.rolePermissions());
    }

    @PreAuthorize("hasAuthority('MODIFY_USER')")
    @PutMapping("/roles/permissions")
    public ResponseEntity<?> updateRolePermission(@RequestBody List<RolePermissionsRequest> rolePermissionsRequests) {
        return ResponseEntity.ok(roleService.updateRolePermissions(rolePermissionsRequests));
    }
}
