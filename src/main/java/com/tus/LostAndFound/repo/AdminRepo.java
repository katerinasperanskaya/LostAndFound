package com.tus.lostAndFound.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.lostAndFound.model.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long>{
	Optional<Admin> findByEmail(String email);

}
