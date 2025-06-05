package com.app.oneplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.domain.AccountStatus;
import com.app.oneplace.exceptions.SellerException;
import com.app.oneplace.model.Seller;
import com.app.oneplace.services.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

	private final SellerService sellerService;
	
	@PatchMapping("/seller/{id}/status/{status}")
	public ResponseEntity<Seller> updateSellerStatus(
			@PathVariable Long id,
			@PathVariable AccountStatus status) throws SellerException{
		
		Seller updatedSeller= sellerService.updateSellerAccountStatus(id, status);
		
		return new ResponseEntity<Seller>(updatedSeller,HttpStatus.OK);
	}

}
