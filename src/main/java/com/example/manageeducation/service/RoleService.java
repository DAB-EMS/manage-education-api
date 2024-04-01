package com.example.manageeducation.service;

import com.example.manageeducation.entity.Role;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface RoleService {
    Role GetRoleById(UUID Id);
}
