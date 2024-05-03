package com.example.manageeducation.service;

import com.example.manageeducation.entity.Authority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorityService {
    List<Authority> syllabusAuthorities (String resource);
}
