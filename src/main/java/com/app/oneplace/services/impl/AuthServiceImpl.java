package com.app.oneplace.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.oneplace.config.JwtProvider;
import com.app.oneplace.domain.USER_ROLE;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.repo.CartRepository;
import com.app.oneplace.repo.UserRepository;
import com.app.oneplace.response.SignupRequest;
import com.app.oneplace.services.AuthService;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;
	
	@Override
	public String createUser(SignupRequest req) {
		
		
		AppUser user = userRepository.findByEmail(req.getEmail());
		if(user != null) {
			AppUser createdUser= new AppUser();
			
			createdUser.setEmail(req.getEmail());
			createdUser.setUsername(req.getFullName());
			createdUser.setRole(USER_ROLE.USER_CUSTOMER);
			createdUser.setMobile("9789489874");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
			
			user = userRepository.save(createdUser);
			
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
			
		}
		List<GrantedAuthority>  authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(USER_ROLE.USER_CUSTOMER.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null,authorities);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return jwtProvider.generateToken(authentication);
	}
	
	
}
