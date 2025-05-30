package com.app.oneplace.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.oneplace.config.JwtProvider;
import com.app.oneplace.domain.USER_ROLE;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.Seller;
import com.app.oneplace.model.VerificationCode;
import com.app.oneplace.repo.CartRepository;
import com.app.oneplace.repo.SellerRepository;
import com.app.oneplace.repo.UserRepository;
import com.app.oneplace.repo.VerificationCodeRepository;
import com.app.oneplace.request.LoginRequest;
import com.app.oneplace.response.AuthResponse;
import com.app.oneplace.response.SignupRequest;
import com.app.oneplace.services.AuthService;
import com.app.oneplace.services.EmailService;
import com.app.oneplace.util.OtpUtil;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;
	private final VerificationCodeRepository verificationCodeRepository;
	private final EmailService emailService;
	private final CustomUserServiceImpl customUserService;
	private final SellerRepository sellerRepository;
	
	@Override
	public String createUser(SignupRequest req) throws Exception {
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
		if(verificationCode ==null || !verificationCode.getOtp().equals(req.getOtp())) {
			throw new Exception("Wrong OTP...");
		}
		
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

	@Override
	public void sentLoginOtp(String email, USER_ROLE role) throws Exception {
		System.out.println("Entry sentLoginOtp method-- email: "+email +" Role : "+role);
		String SIGNIN_PREFIX ="signin_";
		
		if(email.startsWith(SIGNIN_PREFIX)) {
			email =email.substring(SIGNIN_PREFIX.length());
			
			if(role.equals(USER_ROLE.USER_SELLER)) {
				System.out.println("Inside seller check");
				Seller seller = sellerRepository.findByEmail(email);
				if(seller== null) {
					throw new Exception("Seller not found with provided email");
				}
			}
			else {
				System.out.println("Inside customer check");
				AppUser user = userRepository.findByEmail(email);
				if(user== null) {
					throw new Exception("User not exist with provided email");
			}
			
		}
		}
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
		
		if(verificationCode != null) {
			verificationCodeRepository.delete(verificationCode);
		}
		
		String Otp = OtpUtil.generateOtp();
		verificationCode = new VerificationCode();
		verificationCode.setOtp(Otp);
		verificationCode.setEmail(email);
		
		verificationCodeRepository.save(verificationCode);
		
		String subject= "OnePlace Login/SignUp Otp";
		String body ="This is your otp - "+Otp;
		System.out.println("before email otp send email : "+ email+" otp : "+Otp);
		emailService.sendVerificationOtpEmail(email, Otp, subject, body);
		System.out.println("exit email otp send ");
	}

	@Override
	public AuthResponse signin(LoginRequest req) {
		System.out.println("entry : signin method :");
		AuthResponse authResponse = new AuthResponse();
		try {
			String username= req.getEmail();
			String otp = req.getOtp();
			System.out.println("signin method : mail : "+username +" otp : "+otp);
			Authentication authendication = authonticate(username,otp);
			SecurityContextHolder.getContext().setAuthentication(authendication);
			String token = jwtProvider.generateToken(authendication);
			
			authResponse.setJwt(token);
			authResponse.setMessage("Login Success");
			Collection<?extends GrantedAuthority> authorities = authendication.getAuthorities();
			String roleName = (authorities.isEmpty())? null:authorities.iterator().next().getAuthority();
			authResponse.setRole(USER_ROLE.valueOf(roleName));
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("exit : signin method :");
		
		return authResponse;
	}

	private Authentication authonticate(String username, String otp) {
		System.out.println("entry : authonticate method : "+username +" "+otp);
		String SIGNIN_PREFIX ="signin_";

		if(username.startsWith(SIGNIN_PREFIX)) {
			username =username.substring(SIGNIN_PREFIX.length());
		}
		
		UserDetails userDetails= customUserService.loadUserByUsername(username);
		if(userDetails ==null) {
			throw new BadCredentialsException("Username is incorrect");
		}
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
		if(verificationCode ==null || !verificationCode.getOtp().equals(otp)) {
			throw new BadCredentialsException("Wrong Otp");
		}
		System.out.println("exit : authonticate method :");
		return new UsernamePasswordAuthenticationToken(
				userDetails, 
				null,
				userDetails.getAuthorities());
	}
	
	
}
