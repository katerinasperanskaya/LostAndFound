package com.tus.lostAndFound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.lostAndFound.config.TestSecurityConfig;
import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.repo.AdminRepo;
import com.tus.lostAndFound.repo.UserRepo;
import com.tus.lostAndFound.util.JwtUtil;
import com.tus.lostAndFound.util.PasswordEncoderUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminRepo adminRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private PasswordEncoderUtil passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAdminLoginSuccess() throws Exception {
        String rawPassword = "adminpass";
        String encodedPassword = "encoded-adminpass";

        Admin mockAdmin = new Admin();
        mockAdmin.setEmail("admin@email.com");
        mockAdmin.setPassword(encodedPassword);

        when(adminRepo.findByEmail("admin@email.com")).thenReturn(Optional.of(mockAdmin));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(any(UserDetails.class), eq("ADMIN"))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@email.com\",\"password\":\"adminpass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.email").value("admin@email.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testUserLoginSuccess() throws Exception {
        String rawPassword = "userpass";
        String encodedPassword = "encoded-userpass";

        User mockUser = new User();
        mockUser.setEmail("user@email.com");
        mockUser.setPassword(encodedPassword);

        when(adminRepo.findByEmail("user@email.com")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("user@email.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(any(UserDetails.class), eq("USER"))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@email.com\",\"password\":\"userpass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }


    @Test
    public void testLoginInvalidPassword() throws Exception {
        Admin admin = new Admin(1L, "Admin", "admin@email.com", "adminpass", "1234567890");
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.email = "admin@email.com";
        request.password = "wrongpass";

        when(adminRepo.findByEmail("admin@email.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrongpass", "adminpass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        AuthController.AuthRequest request = new AuthController.AuthRequest();
        request.email = "nonexistent@email.com";
        request.password = "any";

        when(adminRepo.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
