package com.example.manageeducation.userservice.role.controller;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "VIEW_USER")
    @DisplayName("List role")
    public void listRoleExpectStatusIsOk() throws Exception {
        mockMvc.perform(get("/api/v1/auth/all-role")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "VIEW_USER")
    @DisplayName("List role permission")
    public void listRolePermissionExpectStatusIsOk() throws Exception {
        mockMvc.perform(get("/api/v1/auth/roles/permissions")).andDo(print())
                .andExpect(status().isOk());
    }
}
