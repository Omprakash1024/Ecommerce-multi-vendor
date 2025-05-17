package com.app.oneplace.services.impl;

import org.springframework.stereotype.Service;

import com.app.oneplace.config.JwtProvider;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.repo.UserRepository;
import com.app.oneplace.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	@Override
	public AppUser findUserByJWTtoken(String JWT) throws Exception {
		String email =jwtProvider.getEmailFromJwtToken(JWT);
		return this.findUserByEmail(email);
	}

	@Override
	public AppUser findUserByEmail(String email) throws Exception {
		AppUser user = userRepository.findByEmail(email);
		if(user ==null) {
			throw new Exception("User not found with email -" + email);
		}
		return user;
	}

}
