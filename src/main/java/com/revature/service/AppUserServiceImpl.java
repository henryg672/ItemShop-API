package com.revature.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.model.AppUser;
import com.revature.model.Role;
import com.revature.repository.RoleRepository;
import com.revature.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional 
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private static final Logger Log = LoggerFactory.getLogger(AppUserServiceImpl.class);
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = userRepository.findByUsername(username);
		if(appUser == null) {
			Log.error("User not found in the database");
			throw new UsernameNotFoundException("User not found in the database");
		} else {
			Log.info("User found in the database: {}", username);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
	}
	
	@Override
	public AppUser saveUser(AppUser appuser) {
		Log.info("Saving new user {} to the database", appuser.getName());
		appuser.setPassword(passwordEncoder.encode(appuser.getPassword()));
		return userRepository.save(appuser);
	}

	@Override
	public Role saveRole(Role role) {
		Log.info("Saving new role {} to the database", role.getName());
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		Log.info("Adding role {} to user {}", roleName, username);
		AppUser appUser = userRepository.findByUsername(username);
		Role role = roleRepository.findByName(roleName);
		appUser.getRoles().add(role);
	}

	@Override
	public AppUser getUser(String username) {
		Log.info("Fetching user {}", username);
		return userRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> getUsers() {
		Log.info("Fetching all users");
		return userRepository.findAll();
	}
}
