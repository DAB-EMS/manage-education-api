package com.example.manageeducation.userservice.service.impl;

import com.example.manageeducation.dto.response.AuthorityResponse;
import com.example.manageeducation.entity.Authority;
import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.repository.AuthorityRepository;
import com.example.manageeducation.repository.RoleRepository;
import com.example.manageeducation.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;
    @Override
    public List<Authority> syllabusAuthorities(String resource) {
        return  authorityRepository.findAllByResource(resource);
    }

    @Override
    public AuthorityResponse getAuthorities(RoleType role, String resource) {
        Optional<Role> roleOptional = roleRepository.findByName(role);
        if(roleOptional.isPresent()){
            Role role1 = roleOptional.get();
            for(Authority authority:role1.getAuthorities()){
                if(authority.getResource().equals(resource)){
                    AuthorityResponse authorityResponse = new AuthorityResponse();
                    authorityResponse.setId(authority.getId());
                    authorityResponse.setResource(authority.getResource());
                    authorityResponse.setPermission(authority.getPermission());
                    return authorityResponse;
                }
            }
        }
        return null;
    }
}
