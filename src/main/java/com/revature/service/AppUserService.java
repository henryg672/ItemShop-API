package com.revature.service;

import java.util.List;

import com.revature.model.AppUser;
import com.revature.model.Role;

public interface AppUserService {
	
	AppUser saveUser(AppUser appuser);
	Role saveRole(Role role);
	void addRoleToUser(String username, String roleName);
	AppUser getUser(String username);
	List<AppUser>getUsers();
}
