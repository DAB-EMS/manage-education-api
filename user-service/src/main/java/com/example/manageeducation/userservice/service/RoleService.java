package com.example.manageeducation.userservice.service;


import com.example.manageeducation.userservice.dto.RolePermissionsRequest;
import com.example.manageeducation.userservice.model.Role;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public interface RoleService {
    Role getRoleByName();
    List<Role> getRoles();
    String updateRolePermissions(List<RolePermissionsRequest> rolePermissionsRequests);
    HashSet<RolePermissionsRequest> rolePermissions();
}
