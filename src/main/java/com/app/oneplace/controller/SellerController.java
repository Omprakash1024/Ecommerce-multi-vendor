package com.app.oneplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import com.app.oneplace.domain.AccountStatus;
import com.app.oneplace.model.Seller;
import com.app.oneplace.model.SellerReport;
import com.app.oneplace.model.VerificationCode;
import com.app.oneplace.repo.SellerRepository;
import com.app.oneplace.repo.VerificationCodeRepository;
import com.app.oneplace.request.LoginRequest;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.response.AuthResponse;
import com.app.oneplace.services.AuthService;
import com.app.oneplace.services.EmailService;
import com.app.oneplace.services.SellerReportService;
import com.app.oneplace.services.SellerService;
import com.app.oneplace.util.OtpUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

	@Autowired
	private final SellerService sellerService;
	private final VerificationCodeRepository verificationCodeRepository;
	private final AuthService authService;
	private final SellerRepository sellerRepository;
	private final EmailService emailService;
	private final SellerReportService sellerReportService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

		String email = req.getEmail();
		req.setEmail("seller_" + email);
		// req.setEmail(email);
		AuthResponse authResponse = authService.signin(req);
		return ResponseEntity.ok(authResponse);
	}

	@PatchMapping("/verify/{otp}")
	public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
		VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
		System.out.println("inside verifySellerEmail pv otp - " + otp + "vc otp - " + verificationCode.getOtp());
		if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new Exception("Wrong otp...");
		}
		Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

		return new ResponseEntity<>(seller, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception, MessagingException {

		Seller saveSeller = sellerService.createSeller(seller);

		String Otp = OtpUtil.generateOtp();
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(Otp);
		verificationCode.setEmail(seller.getEmail());

		verificationCodeRepository.save(verificationCode);

		String subject = "OnePlace email verification code";
		String text = "Welcome to OnePlace, verify your account with this link ";
		String front_end_url = "http://localhost:3000/verify-seller/";
		emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject,
				text + front_end_url);
		return new ResponseEntity<>(saveSeller, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {

		Seller seller = sellerService.getSellerById(id);
		return new ResponseEntity<Seller>(seller, HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {

		Seller seller = sellerService.getSellerProfile(jwt);

		return new ResponseEntity<Seller>(seller, HttpStatus.OK);
	}

	
	 @GetMapping("/report") 
	 public ResponseEntity<SellerReport>
	  					getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception{
	  
		 Seller seller = sellerService.getSellerProfile(jwt);
	  
		 SellerReport sr = sellerReportService.getSellerReport(seller);
	  
	  	return new ResponseEntity<SellerReport>(sr,HttpStatus.OK);
	 }
	  
	 
	@GetMapping()
	public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status)
			throws Exception {

		List<Seller> sellers = sellerService.getAllSellers(status);

		return ResponseEntity.ok(sellers);
	}

	@PatchMapping()
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller)
			throws Exception {

		Seller profile = sellerService.getSellerProfile(jwt);
		Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);

		return ResponseEntity.ok(updatedSeller);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<List<Seller>> deleteSellers(@PathVariable Long id) throws Exception {

		sellerService.deleteSeller(id);

		return ResponseEntity.noContent().build();
	}

}
