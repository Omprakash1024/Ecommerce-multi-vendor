package com.app.oneplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@GetMapping("users/profile") //get user data from DB
	public ResponseEntity<AppUser> createUserHandler(
			@RequestHeader("Authorization") String jwt
			) throws Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		return ResponseEntity.ok(user);
	}
}
