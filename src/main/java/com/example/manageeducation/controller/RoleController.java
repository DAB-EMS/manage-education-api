package com.example.manageeducation.controller;

import com.example.manageeducation.dto.request.RolePermissionsRequest;
import com.example.manageeducation.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/all-role")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.roles());
    }
    @GetMapping("/roles/permissions")
    public ResponseEntity<?> getRolePermission() {
        return ResponseEntity.ok(roleService.rolePermissions());
    }

    @PutMapping("/roles/permissions")
    public ResponseEntity<?> updateRolePermission(@RequestBody List<RolePermissionsRequest> rolePermissionsRequests) {
        return ResponseEntity.ok(roleService.updateRolePermissions(rolePermissionsRequests));
    }
}
