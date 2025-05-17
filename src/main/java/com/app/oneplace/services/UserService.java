package com.app.oneplace.services;

import com.app.oneplace.model.AppUser;

public interface UserService {

	public AppUser findUserByJWTtoken(String JWT) throws Exception;
	public AppUser findUserByEmail(String email) throws Exception;
}
