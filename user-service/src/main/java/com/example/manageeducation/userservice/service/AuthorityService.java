package com.example.manageeducation.userservice.service;

import com.example.manageeducation.userservice.dto.AuthorityResponse;
import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.model.Authority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorityService {
    List<Authority> syllabusAuthorities (String resource);
    AuthorityResponse getAuthorities(RoleType role, String resource);
}
