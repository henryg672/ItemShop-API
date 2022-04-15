package com.revature.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.model.AppUser;
import com.revature.model.Role;
import com.revature.service.AppUserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {

	private final AppUserService appUserService;
	public static final String APPLICATION_JSON_VALUE = "application/json";
	
	@GetMapping("/users")
	public ResponseEntity<List<AppUser>>getUsers() {
		return ResponseEntity.ok().body(appUserService.getUsers());
	}
	
	@PostMapping("/users/save")
	public ResponseEntity<AppUser>saveUser(@RequestBody AppUser appUser) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
		return ResponseEntity.created(uri).body(appUserService.saveUser(appUser));
	}
	
	@PostMapping("/role/save")
	public ResponseEntity<Role>saveRole(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(appUserService.saveRole(role));
	}
	
	@PostMapping("/role/addtouser")
	public ResponseEntity<Role>addRoleToUser(@RequestBody RoleToUserForm form) {
		appUserService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Pass in the String Header
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		//Check if key is not null and starts with Bearer so we know its ours
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				//Get token and remove the word Bearer
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				//Get algorithm
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				//Get verifier
				JWTVerifier verifier = JWT.require(algorithm).build();
				//Get token
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				//Get username
				String username =decodedJWT.getSubject();
				//Find user in database
				AppUser user = appUserService.getUser(username);
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+  10 * 60 *1000))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(FORBIDDEN.value());
				//Get error message
				Map<String, String> error = new HashMap<>();
				//send error message
				error.put("error_message", exception.getMessage());
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}
}


@Data
class RoleToUserForm {
	private String username;
	private String roleName;
}