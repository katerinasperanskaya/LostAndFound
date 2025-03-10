package com.tus.lostAndFound.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.lostAndFound.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
	// Find a user by email
    Optional<User> findByEmail(String email);

    // Check if a user exists by email
    boolean existsByEmail(String email);

}
