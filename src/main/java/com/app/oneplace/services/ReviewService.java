package com.app.oneplace.services;

import java.util.List;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Review;
import com.app.oneplace.request.CreateReviewRequest;

public interface ReviewService {

	Review createReview(CreateReviewRequest req, 
			AppUser user,
			Product product);
	List<Review> getReviewsByProductId(Long productId);
	
	
	Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception ;
	
	void deleteReview(Long reviewId, Long userId) throws Exception;
	
	Review getReviewById(Long reviewId) throws Exception ;
}
