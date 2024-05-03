package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.RoleType;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.RoleRepository;
import com.example.manageeducation.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role GetRoleByName() {
        try{
            Optional<Role> roleOptional = roleRepository.findByName(RoleType.STUDENT);
            if(roleOptional.isPresent()){
                return roleOptional.get();
            }else{
                throw new BadRequestException("Role id is not exist in system.");
            }
        }catch (Exception e){
            throw new BadRequestException("Error with get role by id");
        }
    }

    @Override
    public List<Role> roles() {
        List<Role> roleList = roleRepository.findAll();
        return roleList;
    }
}
