package com.example.manageeducation.service.impl;

import com.example.manageeducation.entity.Role;
import com.example.manageeducation.exception.BadRequestException;
import com.example.manageeducation.repository.RoleRepository;
import com.example.manageeducation.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role GetRoleById(UUID Id) {
        try{
            Optional<Role> roleOptional = roleRepository.findById(Id);
            if(roleOptional.isPresent()){
                return roleOptional.get();
            }else{
                throw new BadRequestException("Role id is not exist in system.");
            }
        }catch (Exception e){
            throw new BadRequestException("Error with get role by id");
        }
    }
}
