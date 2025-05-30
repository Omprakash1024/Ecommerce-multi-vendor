package com.app.oneplace.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.oneplace.domain.USER_ROLE;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Seller;
import com.app.oneplace.repo.SellerRepository;
import com.app.oneplace.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService{

	private final UserRepository userRepository;
	private final SellerRepository sellerRepository;
	
	private static final String SELLER_PREFIX ="seller_"; // if any usr name is starts with seller the need to check seller table else appuser table
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("entry : loadUserByUsername :");
		if(username.startsWith(SELLER_PREFIX)) {
			String actualUserName = username.substring(CustomUserServiceImpl.SELLER_PREFIX.length());
			Seller seller = sellerRepository.findByEmail(actualUserName);
			if(seller!=null) {
				return buildUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());
			}
		}else {
			System.out.println("entry : loadUserByUsername : user block");
			AppUser user = userRepository.findByEmail(username);
			System.out.println("entry : loadUserByUsername : user block after");
			if(user!=null) {
				return buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());
			}
		}
		System.out.println("exit : loadUserByUsername");
		return (UserDetails) new UsernameNotFoundException("User or Seller not found with the email - "+username);
	}
	private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
		if(role ==null) role = USER_ROLE.USER_CUSTOMER;
		
		List<GrantedAuthority> authorityList = new ArrayList<>();
		
		authorityList.add(new SimpleGrantedAuthority(String.valueOf(role))); 
		return new User(email,password, authorityList);
	}

}
