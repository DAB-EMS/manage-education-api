package com.example.manageeducation.userservice.role.service;

import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.model.Role;
import com.example.manageeducation.userservice.repository.RoleRepository;
import com.example.manageeducation.userservice.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoleService roleService;

    private final UUID roleA = UUID.randomUUID();
    private final UUID roleB = UUID.randomUUID();
    private final UUID roleC = UUID.randomUUID();

    @BeforeEach
    public void setup() {

        List<Role> roles = new ArrayList<>(
                List.of(
                        Role.builder().id(roleA).name(RoleType.STUDENT).authorities(null).build(),
                        Role.builder().id(roleB).name(RoleType.TRAINER).authorities(null).build(),
                        Role.builder().id(roleC).name(RoleType.CLASS_ADMIN).authorities(null).build()
                )
        );

        Optional<Role> role1 = Optional.ofNullable(roles.get(0));
        Optional<Role> role2 = Optional.ofNullable(roles.get(1));
        Optional<Role> role3 = Optional.ofNullable(roles.get(2));

        Mockito.when(roleRepository.findById(roleA)).thenReturn(role1);
        Mockito.when(roleRepository.findById(roleB)).thenReturn(role2);
        Mockito.when(roleRepository.findById(roleC)).thenReturn(role3);
//
        Mockito.when(roleService.getRoles()).thenReturn(roles);
    }

    @Test
    @DisplayName("Get All Roles")
    public void getAllRoles() {
        List<Role> result = roleService.getRoles();
        Assertions.assertThat(result.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("Get All Roles V2")
    public void getAllRolesV2() {
        ResponseEntity<String> result = restTemplate.getForEntity("/roles", String.class);

    }


}
