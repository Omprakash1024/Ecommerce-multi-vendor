package com.app.oneplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Review;
import com.app.oneplace.request.CreateReviewRequest;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.services.ProductService;
import com.app.oneplace.services.ReviewService;
import com.app.oneplace.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

	private final ReviewService reviewService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<List<Review>> getReviewByProductId(
			@PathVariable Long productId) throws Exception{
		
		List<Review> reviews = reviewService.getReviewsByProductId(productId);
		
		 return new ResponseEntity<>(reviews, HttpStatus.OK);
		
	}
	
	@PostMapping("/products/{productId}/reviews")
	public ResponseEntity<Review> writeReview(
			@RequestBody CreateReviewRequest req,
			@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		Product product = productService.findProductById(productId);
		
		Review review = reviewService.createReview(req, user, product);
		 return new ResponseEntity<>(review, HttpStatus.OK);
	}
	
	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<Review> updateReview(
			@RequestBody CreateReviewRequest req,
			@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		
		Review review = reviewService.updateReview(reviewId, req.getReviewText(), req.getReviewRating(), user.getId());
		return new ResponseEntity<>(review, HttpStatus.OK);
	}
	
	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse> deleteReview(
			@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		
		reviewService.deleteReview(reviewId, user.getId());
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Review deleted successfully...");
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
