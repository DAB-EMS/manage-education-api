package com.example.manageeducation.userservice.service.impl;

import com.example.manageeducation.userservice.dto.RolePermissionsRequest;
import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.exception.BadRequestException;
import com.example.manageeducation.userservice.model.Authority;
import com.example.manageeducation.userservice.model.Role;
import com.example.manageeducation.userservice.repository.AuthorityRepository;
import com.example.manageeducation.userservice.repository.RoleRepository;
import com.example.manageeducation.userservice.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    ModelMapper modelMapper;

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
    public List<Role> getRoles() {
        List<Role> roleList = roleRepository.findAll();
        return roleList;
    }

    @Override
    @Transactional
    public String updateRolePermissions(List<RolePermissionsRequest> rolePermissionsRequests) {
        try {
            UUID superAdminId;
            Optional<Role> optionalRole = roleRepository.findByName(RoleType.SUPER_ADMIN);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                superAdminId = role.getId();
            } else {
                superAdminId = null;
                throw new BadRequestException("SUPER_ADMIN not found.");
            }



            boolean responseEntityValidateForm = validateUpdateRolePermissions(rolePermissionsRequests, superAdminId);
            if (!responseEntityValidateForm) {
                return "validation role permission";
            }

            rolePermissionsRequests.stream()
                    .filter(updateRolePermissionsForm -> !superAdminId.equals(updateRolePermissionsForm.getRoleId()))
                    .filter(updateRolePermissionsForm -> updateRolePermissionsForm.getAuthoritiesId().size() == 5)
                    .collect(Collectors.toList())
                    .forEach(updateRolePermissionsForm -> {
                        Set<Authority> authorities = new HashSet<Authority>();

                        updateRolePermissionsForm.getAuthoritiesId().forEach(id -> {
                            Authority authority = authorityRepository.findOneById(id).orElseThrow(() -> new BadRequestException("Authority id not found"));
                            authorities.add(authority);
                        });

                        Role role = roleRepository.findById(updateRolePermissionsForm.getRoleId()).orElseThrow(() -> new BadRequestException("Role id is not found."));
                        role.setAuthorities(authorities);
                        roleRepository.save(role);
                    });

            return "Update role's permissions successfully.";

        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public HashSet<RolePermissionsRequest> rolePermissions() {
        HashSet<RolePermissionsRequest> rolePermissionsRequests = new HashSet<>();
        List<Role> roles = roleRepository.findAll();
        for(Role role:roles){
            RolePermissionsRequest rolePermissionsRequest = new RolePermissionsRequest();
            rolePermissionsRequest.setRoleId(role.getId());
            List<UUID> authorities = new ArrayList<>();
            for(Authority authority:role.getAuthorities()){
                authorities.add(authority.getId());
            }
            rolePermissionsRequest.setAuthoritiesId(authorities);
            rolePermissionsRequests.add(rolePermissionsRequest);
        }
        return rolePermissionsRequests;
    }

    private boolean validateUpdateRolePermissions(List<RolePermissionsRequest> updateRolePermissionsForms, UUID superAdminId) {
        if (updateRolePermissionsForms.size() == 1
                && updateRolePermissionsForms
                .stream()
                .anyMatch(updateRolePermissionsForm -> superAdminId.equals(updateRolePermissionsForm.getRoleId()))) {
            return false;
        }

        for (RolePermissionsRequest updateRolePermissionsForm : updateRolePermissionsForms) {
            boolean checkDuplicateAuthoritiesIdResponseObject = this.checkDuplicateAuthoritiesId(updateRolePermissionsForm.getAuthoritiesId());
            if (!checkDuplicateAuthoritiesIdResponseObject) {
                return false;
            }

            boolean checkAuthoritiesIdListSizeResponseObject = this.checkAuthoritiesIdListSize(updateRolePermissionsForm.getAuthoritiesId());
            if (!checkAuthoritiesIdListSizeResponseObject) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDuplicateAuthoritiesId(List<UUID> authoritiesId) {
        Set<UUID> duplicateIdList = new HashSet<>();
        Set<String> duplicateResourceList = new HashSet<>();

        Authority authorityCurrent = new Authority();
        Authority authorityNext = new Authority();
        for (int i = 0; i < authoritiesId.size(); i++) {
            UUID authorityIdCurrent = authoritiesId.get(i);
            authorityCurrent = authorityRepository.findOneById(authorityIdCurrent).orElseThrow(() -> new BadRequestException("Authority id not found"));
            for (int j = i + 1; j < authoritiesId.size(); j++) {
                if (authoritiesId.get(i).compareTo(authoritiesId.get(j)) == 0) {
                    duplicateIdList.add(authoritiesId.get(i));
                }
                else {
                    UUID authorityIdNext = authoritiesId.get(j);
                    authorityNext = authorityRepository.findOneById(authoritiesId.get(j)).orElseThrow(() -> new BadRequestException("Authority id not found"));
                    if (authorityCurrent.getResource().equals(authorityNext.getResource())) {
                        duplicateResourceList.add(authorityCurrent.getResource());
                    }
                }

            }
        }

        if (!duplicateIdList.isEmpty()) {
            return false;
        }
        if (!duplicateResourceList.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkAuthoritiesIdListSize(List<UUID> authoritiesId) {
        if (authoritiesId.size() != 5) {
            return false;
        }
        return true;

    }
}
