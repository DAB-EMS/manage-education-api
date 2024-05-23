package com.example.manageeducation.userservice.service;

import com.example.manageeducation.dto.response.AuthorityResponse;
import com.example.manageeducation.entity.Authority;
import com.example.manageeducation.enums.RoleType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorityService {
    List<Authority> syllabusAuthorities (String resource);
    AuthorityResponse getAuthorities(RoleType role, String resource);
}
