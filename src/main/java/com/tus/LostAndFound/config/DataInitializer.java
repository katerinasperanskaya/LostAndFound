package com.tus.lostAndFound.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.repo.AdminRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepo adminRepo;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private final String DEFAULT_ADMIN_EMAIL = "admin@lostandfound.com";
    private final String DEFAULT_ADMIN_PASSWORD = "Admin@123";

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin with the default email exists
        if (adminRepo.findByEmail(DEFAULT_ADMIN_EMAIL).isEmpty()) {
            Admin admin = new Admin();
            admin.setName("Super Admin");
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setPhone("1234567890");
            
//            // Hash the password before saving
//            admin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
            admin.setPassword(DEFAULT_ADMIN_PASSWORD);

            adminRepo.save(admin);
            System.out.println("Default admin account created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("Admin already exists. Skipping initialization.");
        }
    }
}

