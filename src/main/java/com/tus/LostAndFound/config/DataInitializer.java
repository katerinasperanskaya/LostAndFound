package com.tus.lostAndFound.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.model.User; // Assuming you have a User entity
import com.tus.lostAndFound.repo.AdminRepo;
import com.tus.lostAndFound.repo.UserRepo;
import com.tus.lostAndFound.util.PasswordEncoderUtil;

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

    @Autowired
private PasswordEncoder passwordEncoder;

@Override
public void run(String... args) throws Exception {
    if (adminRepo.findByEmail(DEFAULT_ADMIN_EMAIL).isEmpty()) {
        Admin admin = new Admin();
        admin.setName("Super Admin");
        admin.setEmail(DEFAULT_ADMIN_EMAIL);
        admin.setPhone("1234567890");
        admin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD)); // Hash password
        adminRepo.save(admin);
        System.out.println("Default admin account created: " + DEFAULT_ADMIN_EMAIL);
    } else {
        System.out.println("Admin already exists. Skipping initialization.");
    }

    if (userRepo.findByEmail(DEFAULT_USER_EMAIL).isEmpty()) {
        User user = new User();
        user.setName("Default User");
        user.setEmail(DEFAULT_USER_EMAIL);
        user.setPhone("0987654321");
        user.setPassword(passwordEncoder.encode(DEFAULT_USER_PASSWORD)); // Hash password
        userRepo.save(user);
        System.out.println("Default user account created: " + DEFAULT_USER_EMAIL);
    } else {
        System.out.println("User already exists. Skipping initialization.");
    }
}
}