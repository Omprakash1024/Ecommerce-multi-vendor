package com.app.oneplace.services;

import com.app.oneplace.request.LoginRequest;
import com.app.oneplace.response.AuthResponse;
import com.app.oneplace.response.SignupRequest;

public interface AuthService {
	
	void sentLoginOtp(String email) throws Exception;
	String createUser (SignupRequest req) throws Exception;
	AuthResponse signin(LoginRequest req);
}
