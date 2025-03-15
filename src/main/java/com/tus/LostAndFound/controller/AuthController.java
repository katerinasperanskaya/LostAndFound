package com.tus.lostAndFound.controller;

import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.repo.UserRepo;
import com.tus.lostAndFound.util.JwtUtil;
import com.tus.lostAndFound.util.PasswordEncoderUtil;
import com.tus.lostAndFound.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoderUtil passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
        public String token; // Add this field

        // Constructor with four parameters
        public AuthResponse(String message, String email, String role, String token) {
            this.message = message;
            this.email = email;
            this.role = role;
            this.token = token; // Assign the token
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        Optional<Admin> adminOptional = adminRepo.findByEmail(authRequest.email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (passwordEncoder.matches(authRequest.password, admin.getPassword())) {
                String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                        admin.getEmail(), admin.getPassword(), new ArrayList<>()), "ADMIN");
                return ResponseEntity.ok(new AuthResponse("Login successful", admin.getEmail(), "ADMIN", token));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", authRequest.email, "ADMIN", null));
            }
        }

        Optional<User> userOptional = userRepo.findByEmail(authRequest.email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(authRequest.password, user.getPassword())) {
                String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), new ArrayList<>()), "USER");
                return ResponseEntity.ok(new AuthResponse("Login successful", user.getEmail(), "USER", token));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", authRequest.email, "USER", null));
            }
        }

        return ResponseEntity.status(404).body(new AuthResponse("User not found", authRequest.email, "NONE", null));
    }
}