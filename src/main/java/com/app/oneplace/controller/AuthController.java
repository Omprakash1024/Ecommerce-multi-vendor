package com.app.oneplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.domain.USER_ROLE;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.VerificationCode;
import com.app.oneplace.repo.UserRepository;
import com.app.oneplace.request.LoginOTPRequest;
import com.app.oneplace.request.LoginRequest;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.response.AuthResponse;
import com.app.oneplace.response.SignupRequest;
import com.app.oneplace.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final UserRepository userRepository;
	@PostMapping("/signup") //creating user
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest request) throws Exception{
		
		String jwt = authService.createUser(request);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register success");
		authResponse.setRole(USER_ROLE.USER_CUSTOMER);
		return ResponseEntity.ok(authResponse);
	}
	@PostMapping("/send/login-signup-otp") //otp generation for Login/Signup
	public ResponseEntity<ApiResponse> sendOTPHandler(@RequestBody LoginOTPRequest request) throws Exception{
		
		authService.sentLoginOtp(request.getEmail(),request.getRole());
		ApiResponse response = new ApiResponse();
		response.setMessage("OTP send successfully");
		return ResponseEntity.ok(response);
	}
	@PostMapping("/signing") //login
	public ResponseEntity<AuthResponse> loginHandler(
			@RequestBody LoginRequest request) throws Exception{
		
		
		AuthResponse response = authService.signin(request);
		return ResponseEntity.ok(response);
	}
}
