package com.tus.lostAndFound.service;

import com.tus.lostAndFound.model.Admin;
import com.tus.lostAndFound.model.User;
import com.tus.lostAndFound.repo.AdminRepo;
import com.tus.lostAndFound.repo.UserRepo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if the email belongs to an admin
        Admin admin = adminRepo.findByEmail(email)
                .orElse(null);

        if (admin != null) {
            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    AuthorityUtils.createAuthorityList("ADMIN") // Assign ADMIN role
            );
        }

        // Check if the email belongs to a user
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("USER") // Assign USER role
        );
    }
}