package com.tus.lostAndFound.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.model.User; // Assuming you have a User entity
import com.tus.lostAndFound.repo.AdminRepo;
import com.tus.lostAndFound.repo.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private UserRepo userRepo;

    private final String DEFAULT_ADMIN_EMAIL = "admin";
    private final String DEFAULT_ADMIN_PASSWORD = "admin";
    private final String DEFAULT_USER_EMAIL = "user";
    private final String DEFAULT_USER_PASSWORD = "user";

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin with the default email exists
        if (adminRepo.findByEmail(DEFAULT_ADMIN_EMAIL).isEmpty()) {
            Admin admin = new Admin();
            admin.setName("Super Admin");
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setPhone("1234567890");

            // Set the plain-text password (no encoding)
            admin.setPassword(DEFAULT_ADMIN_PASSWORD);

            adminRepo.save(admin);
            System.out.println("Default admin account created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("Admin already exists. Skipping initialization.");
        }

        // Check if a user with the default email exists
        if (userRepo.findByEmail(DEFAULT_USER_EMAIL).isEmpty()) {
            User user = new User(); // Use the User entity here
            user.setName("Default User");
            user.setEmail(DEFAULT_USER_EMAIL);
            user.setPhone("0987654321");

            // Set the plain-text password (no encoding)
            user.setPassword(DEFAULT_USER_PASSWORD);

            userRepo.save(user);
            System.out.println("Default user account created: " + DEFAULT_USER_EMAIL);
        } else {
            System.out.println("User already exists. Skipping initialization.");
        }
    }
}