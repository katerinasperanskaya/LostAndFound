package com.tus.lostAndFound.service;

import com.tus.lostAndFound.dto.UserDTO;
import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    // Retrieve all users (returning DTOs)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Find user by ID (returning DTO)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepo.findById(id).map(this::convertToDTO);
    }

    // Find user by email (returning DTO)
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepo.findByEmail(email).map(this::convertToDTO);
    }

    // Create a new user (using the model)
    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        return userRepo.save(user);
    }

    // Update an existing user (excluding password updates for security)
    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
        return userRepo.findById(id).map(user -> {
            user.setName(updatedUserDTO.getName());
            user.setEmail(updatedUserDTO.getEmail());
            user.setPhone(updatedUserDTO.getPhone());
            // Do not update the password here
            return convertToDTO(userRepo.save(user));
        }).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    // Delete a user
    public boolean deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // Convert User entity to DTO
    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }
}