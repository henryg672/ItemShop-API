package com.revature;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.revature.model.AppUser;
import com.revature.model.Role;
import com.revature.service.AppUserService;


@SpringBootApplication
public class Project1Application {

	public static void main(String[] args) {
		SpringApplication.run(Project1Application.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(AppUserService appUserService) {
		return args -> {
			appUserService.saveRole(new Role(null, "ROLE_USER"));
			appUserService.saveRole(new Role(null, "ROLE_CUSTOMER"));
			appUserService.saveRole(new Role(null, "ROLE_EMPLOYEE"));
			
			appUserService.saveUser(new AppUser(null, "John Travolta", "john", "1234", new ArrayList<>()));
			appUserService.saveUser(new AppUser(null, "Jim Carry", "jim", "1234", new ArrayList<>()));
			appUserService.saveUser(new AppUser(null, "Will Smith", "will", "1234", new ArrayList<>()));
		
			appUserService.addRoleToUser("john", "ROLE_USER");
			appUserService.addRoleToUser("jim", "ROLE_CUSTOMER");
			appUserService.addRoleToUser("will", "ROLE_EMPLOYEE");
		};
	}
}
