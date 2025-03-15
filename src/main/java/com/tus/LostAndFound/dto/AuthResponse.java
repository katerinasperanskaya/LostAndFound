package com.tus.lostAndFound.dto;

// DTO for responses
public class AuthResponse {
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