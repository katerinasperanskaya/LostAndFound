package com.tus.lostAndFound.service;


import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    // Retrieve all admins
    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }

    // Find admin by ID
    public Optional<Admin> getAdminById(Long id) {
        return adminRepo.findById(id);
    }

    // Find admin by email
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepo.findByEmail(email);
    }

    // Create a new admin (prevents duplicate email)
    public Admin createAdmin(Admin admin) {
        if (adminRepo.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("An admin with this email already exists.");
        }
        return adminRepo.save(admin);
    }

    // Update an existing admin
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        return adminRepo.findById(id).map(admin -> {
            admin.setName(updatedAdmin.getName());
            admin.setEmail(updatedAdmin.getEmail());
            admin.setPassword(updatedAdmin.getPassword());
            return adminRepo.save(admin);
        }).orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + id));
    }

    // Delete an admin
    public boolean deleteAdmin(Long id) {
        if (adminRepo.existsById(id)) {
            adminRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

