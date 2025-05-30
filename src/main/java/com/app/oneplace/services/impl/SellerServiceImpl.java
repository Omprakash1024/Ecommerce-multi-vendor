package com.app.oneplace.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.oneplace.config.JwtProvider;
import com.app.oneplace.domain.USER_ROLE;
import com.app.oneplace.exceptions.SellerException;
import com.app.oneplace.model.Address;
import com.app.oneplace.model.Seller;
import com.app.oneplace.repo.AddressRepository;
import com.app.oneplace.repo.SellerRepository;
import com.app.oneplace.services.SellerService;
import com.app.oneplace.domain.AccountStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{
	
	private final SellerRepository sellerRepository;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;

	@Override
	public Seller getSellerProfile(String jwt) throws Exception {
		Seller seller = new Seller();
		try {
			String email = jwtProvider.getEmailFromJwtToken(jwt);
			seller =  this.getSellerByEmail(email);
		}catch(Exception e) {
			throw new Exception("Seller Not Found...");
		}
		
		return seller;
	}

	@Override
	public Seller createSeller(Seller seller) throws Exception {
		Seller sellerExists = sellerRepository.findByEmail(seller.getEmail());
		if(sellerExists != null) {
			throw new Exception("Seller already exists, Use different email");
		}
		System.out.println("Entry createSeller method email: "+ seller.getEmail());
		Address savedAddress =addressRepository.save(seller.getPickupAddress());
		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAddress(savedAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(USER_ROLE.USER_SELLER);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBankDetails(seller.getBankDetails());
		newSeller.setBusinessDetails(seller.getBusinessDetails());
	
		return sellerRepository.save(newSeller);
	}

	@Override
	public Seller getSellerById(Long id) throws SellerException {
		Optional<Seller> seller = sellerRepository.findById(id);
		if(!seller.isPresent()) {
		  throw	new SellerException("Seller not found with ID - "+ id);
		}
		return seller.get();
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		Seller seller = sellerRepository.findByEmail(email);
		if(seller ==null) {
			throw new Exception("Seller Not Found...");
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws SellerException {
		
		Seller updatedSeller = this.getSellerById(id);
		
		if(seller.getSellerName() != null) {
			updatedSeller.setSellerName(seller.getSellerName());
		}
		if(seller.getMobile() !=null) {
			updatedSeller.setMobile(seller.getMobile());
		}
		if(seller.getEmail() != null) {
			updatedSeller.setEmail(seller.getEmail());
		}
		if(seller.getBusinessDetails() != null 
				&& seller.getBusinessDetails().getBusinessName() != null){
			
			updatedSeller.getBusinessDetails().setBusinessName(
					seller.getBusinessDetails().getBusinessName()
					);
		}
		if(seller.getBankDetails() != null
				&& seller.getBankDetails().getAccountHolderName() != null
				&& seller.getBankDetails().getIfscCode() != null
				&& seller.getBankDetails().getAccountNumber() != null) {
			
			updatedSeller.getBankDetails().setAccountHolderName(
					seller.getBankDetails().getAccountHolderName());
			updatedSeller.getBankDetails().setIfscCode(
					seller.getBankDetails().getIfscCode());
			updatedSeller.getBankDetails().setAccountNumber(
					seller.getBankDetails().getAccountNumber());
		}
		if(seller.getPickupAddress() != null
				&& seller.getPickupAddress().getAddress() != null
				&& seller.getPickupAddress().getMobile() != null
				&& seller.getPickupAddress().getCity() != null
				&& seller.getPickupAddress().getState() != null) {
			updatedSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
			updatedSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
			updatedSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
			updatedSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
		}
		if(seller.getGSTIN() != null) {
			updatedSeller.setGSTIN(seller.getGSTIN());
		}
			
		return sellerRepository.save(updatedSeller);
	}

	@Override
	public void deleteSeller(Long id) throws SellerException {
		Seller seller = this.getSellerById(id);
		sellerRepository.delete(seller);
		
	}

	@Override
	public Seller verifyEmail(String email, String otp) throws Exception {
		Seller seller = getSellerByEmail(email);
		seller.setEmailVerfied(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws SellerException {
		Seller seller = this.getSellerById(id);
		seller.setAccountStatus(status);
		return sellerRepository.save(seller);
	}

}
