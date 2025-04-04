
package com.tus.lostAndFound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.lostAndFound.dto.UserDTO;
import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.service.UserService;
import com.tus.lostAndFound.util.JwtUtil;
import com.tus.lostAndFound.util.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoderUtil passwordEncoderUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");

        userDTO = new UserDTO(1L, "John Doe", "john@example.com", "1234567890");
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetUserById_Found() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetUserByEmail_Found() throws Exception {
        when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/email?email=john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testGetUserByEmail_NotFound() throws Exception {
        when(userService.getUserByEmail("missing@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/email?email=missing@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testCreateUser_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testCreateUser_DuplicateEmail() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("User already exists"));

        mockMvc.perform(post("/api/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(1L, userDTO)).thenReturn(userDTO);

        String response = mockMvc.perform(put("/api/users/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(999L), any(UserDTO.class)))
                .thenThrow(new IllegalArgumentException("User not found with ID: 999"));

        mockMvc.perform(put("/api/users/999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteUser_Success() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/999").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
