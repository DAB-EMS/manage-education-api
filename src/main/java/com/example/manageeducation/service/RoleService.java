package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.RolePermissionsRequest;
import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.RoleType;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public interface RoleService {
    Role GetRoleByName();
    List<Role> roles();
    String updateRolePermissions(List<RolePermissionsRequest> rolePermissionsRequests);
    HashSet<RolePermissionsRequest> rolePermissions();
}
