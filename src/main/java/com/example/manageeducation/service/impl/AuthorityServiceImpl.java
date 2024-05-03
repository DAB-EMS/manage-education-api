package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.Authority;
import com.example.manageeducation.repository.AuthorityRepository;
import com.example.manageeducation.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;
    @Override
    public List<Authority> syllabusAuthorities(String resource) {
        return  authorityRepository.findAllByResource(resource);
    }
}
