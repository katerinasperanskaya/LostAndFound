package com.tus.lostAndFound.service;


import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.repo.AdminRepo;
import com.tus.lostAndFound.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private AdminRepo adminRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Admin() {
        Admin admin = new Admin(1L, "Admin", "admin@email.com", "123456", "adminPass");

        when(adminRepo.findByEmail("admin@email.com")).thenReturn(Optional.of(admin));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin@email.com");

        assertEquals("admin@email.com", userDetails.getUsername());
        assertEquals("adminPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")));
    }

    @Test
    void testLoadUserByUsername_User() {
        when(adminRepo.findByEmail("user@email.com")).thenReturn(Optional.empty());

        User user = new User(2L, "User", "user@email.com", "987654", "userPass");
        when(userRepo.findByEmail("user@email.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user@email.com");

        assertEquals("user@email.com", userDetails.getUsername());
        assertEquals("userPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")));
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        String email = "ghost@email.com";

        // BOTH must return Optional.empty() to trigger UsernameNotFoundException
        when(adminRepo.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
            customUserDetailsService.loadUserByUsername(email)
        );

        assertEquals("User not found with email: " + email, exception.getMessage());
    }


}
