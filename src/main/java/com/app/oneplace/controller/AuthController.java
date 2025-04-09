package com.app.oneplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.repo.UserRepository;
import com.app.oneplace.response.SignupRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserRepository userRepository;
	@PostMapping("/signup")
	public ResponseEntity<AppUser> createUserHandler(@RequestBody SignupRequest request){
		
		AppUser user = new AppUser();
		user.setEmail(request.getEmail());
		user.setUsername(request.getFullName());
		AppUser savedUser = userRepository.save(user);
		return ResponseEntity.ok(savedUser);
	}
}
