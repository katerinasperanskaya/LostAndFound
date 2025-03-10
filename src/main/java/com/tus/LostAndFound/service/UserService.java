package com.tus.lostAndFound.service;


import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    // Retrieve all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Find user by ID
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    // Find user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    // Create a new user (prevents duplicate email)
    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }
        return userRepo.save(user);
    }

    // Update an existing user
    public User updateUser(Long id, User updatedUser) {
        return userRepo.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            return userRepo.save(user);
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
}

