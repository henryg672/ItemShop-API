package com.revature.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(String name);
}