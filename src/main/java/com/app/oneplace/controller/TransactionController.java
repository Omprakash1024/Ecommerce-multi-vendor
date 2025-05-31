package com.app.oneplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.Seller;
import com.app.oneplace.model.Transaction;
import com.app.oneplace.services.SellerService;
import com.app.oneplace.services.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

	private final TransactionService transactionService;
	private final SellerService sellerService;
	
	
	@GetMapping("/seller")
	public ResponseEntity<List<Transaction>> getTransactionBySeller(
			
			@RequestHeader("Authorization") String jwt) throws  Exception{
	
		Seller seller = sellerService.getSellerProfile(jwt);
		List<Transaction> transactions = transactionService.getTransactionBySellerId(seller);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
		
	}
	
	@GetMapping()
	public ResponseEntity<List<Transaction>> getAllTransactions(){ 
		
		List<Transaction> transactions = transactionService.getAllTransaction();
		return new ResponseEntity<>(transactions,HttpStatus.OK);
		
	}
}
