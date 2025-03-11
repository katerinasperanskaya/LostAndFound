package com.tus.lostAndFound.controller;

import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.repo.UserRepo;
import com.tus.lostAndFound.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminRepo adminRepo;

    // DTO for login requests
    public static class AuthRequest {
        public String email;
        public String password;
    }

    // DTO for responses
    public static class AuthResponse {
        public String message;
        public String email;
        public String role;

        public AuthResponse(String message, String email, String role) {
            this.message = message;
            this.email = email;
            this.role = role;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        // Check in Admin table first
        Optional<Admin> adminOptional = adminRepo.findByEmail(authRequest.email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (admin.getPassword().equals(authRequest.password)) {
                return ResponseEntity.ok(new AuthResponse("Login successful", admin.getEmail(), "ADMIN"));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", authRequest.email, "ADMIN"));
            }
        }

        // Check in User table if not found in Admin
        Optional<User> userOptional = userRepo.findByEmail(authRequest.email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(authRequest.password)) {
                return ResponseEntity.ok(new AuthResponse("Login successful", user.getEmail(), "USER"));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", authRequest.email, "USER"));
            }
        }

        // If email not found in either table
        return ResponseEntity.status(404).body(new AuthResponse("User not found", authRequest.email, "NONE"));
    }
}
