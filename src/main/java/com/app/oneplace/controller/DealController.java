package com.app.oneplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.Deal;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.services.DealService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

	private final DealService dealService;
	
	@PostMapping()
	public ResponseEntity<Deal> createDeal(
			@RequestBody Deal deal){
		
		Deal createdDeal = dealService.createDeal(deal);
		
		return new ResponseEntity<>(createdDeal, HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Deal> updateDeal(
			@PathVariable Long id,
			@RequestBody Deal deal
			) throws Exception{
		
		Deal updatedDeal = dealService.updateDeal(deal,id);
		
		
		return new ResponseEntity<>(updatedDeal, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteDeal(
			@PathVariable Long id
			) throws Exception{
		
		dealService.deleteDeal(id);
		
		ApiResponse apiResponse = new ApiResponse();
		
		apiResponse.setMessage("Deal deleted successfully");
		
		return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
	}
}
