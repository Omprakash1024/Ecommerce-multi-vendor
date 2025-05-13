package com.app.oneplace.services;

import com.app.oneplace.response.SignupRequest;

public interface AuthService {
	
	String createUser (SignupRequest req);
}
