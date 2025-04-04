package com.tus.lostAndFound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin1");
        admin.setEmail("admin@email.com");
        admin.setPhone("1234567890");
        admin.setPassword("pass123");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAllAdmins() throws Exception {
        Mockito.when(adminService.getAllAdmins()).thenReturn(Arrays.asList(admin));

        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAdminById() throws Exception {
        Mockito.when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin1"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAdminByEmail() throws Exception {
        Mockito.when(adminService.getAdminByEmail("admin@email.com")).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/api/admins/email")
                        .param("email", "admin@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@email.com"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testCreateAdmin() throws Exception {
        Mockito.when(adminService.createAdmin(any(Admin.class))).thenReturn(admin);

        mockMvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin1"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testUpdateAdmin() throws Exception {
        Mockito.when(adminService.updateAdmin(eq(1L), any(Admin.class))).thenReturn(admin);

        mockMvc.perform(put("/api/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteAdmin_Success() throws Exception {
        Mockito.when(adminService.deleteAdmin(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/admins/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testDeleteAdmin_NotFound() throws Exception {
        Mockito.when(adminService.deleteAdmin(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/admins/1"))
                .andExpect(status().isNotFound());
    }
}
