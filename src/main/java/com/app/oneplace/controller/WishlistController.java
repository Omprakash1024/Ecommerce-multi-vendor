package com.app.oneplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Wishlist;
import com.app.oneplace.services.ProductService;
import com.app.oneplace.services.UserService;
import com.app.oneplace.services.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

	private final WishlistService wishlistService;
	private final UserService userService;
	private final ProductService productService;
	
	
	@GetMapping()
	public ResponseEntity<Wishlist> getWishlistByUserId(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		
		Wishlist wishlist= wishlistService.getWishlistByUserId(user);
		 return new ResponseEntity<>(wishlist, HttpStatus.OK);
		
	}
	
	@PutMapping("/add-product/{productId}")
	public ResponseEntity<Wishlist> addProductToWishlist(
			@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws ProductException, Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		Product product = productService.findProductById(productId);
		
		Wishlist wishlist = wishlistService.addProductToWishlist(user, product);
		
	    return new ResponseEntity<>(wishlist, HttpStatus.OK);
		
	}
}
