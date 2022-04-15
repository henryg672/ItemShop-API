package com.revature.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	
	AppUser findByUsername(String username);
}
