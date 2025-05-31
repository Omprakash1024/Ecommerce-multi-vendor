package com.app.oneplace.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Review;
import com.app.oneplace.repo.ReviewRepository;
import com.app.oneplace.request.CreateReviewRequest;
import com.app.oneplace.services.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
	
	private final ReviewRepository reviewRepository;
	
	@Override
	public Review createReview(CreateReviewRequest req, AppUser user, Product product) {
		
		Review review= new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReviewText(req.getReviewText());
		review.setRating(req.getReviewRating());
		review.setProductImages(req.getProductImages());
		product.getReviews().add(review);
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getReviewsByProductId(Long productId) {
		
		return reviewRepository.findByProductId(productId);
	}

	@Override
	public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
		Review review =getReviewById(reviewId);
		
		
		if(!review.getUser().getId().equals(userId)) {
			throw new Exception("You can't update this review");
		}else {
			review.setReviewText(reviewText);
			review.setRating(rating);
		}
		return review;
	}

	@Override
	public void deleteReview(Long reviewId, Long userId) throws Exception {
		Review review =getReviewById(reviewId);
		
       if(review ==null) {
    	   throw new Exception("review doesn't exist...");
		}
		
		if(!review.getUser().getId().equals(userId)) {
			throw new Exception("You can't delete this review...");
		}
		reviewRepository.delete(review);
	}

	@Override
	public Review getReviewById(Long reviewId) throws Exception {
		
		return reviewRepository.findById(reviewId).orElseThrow(()-> new Exception("Review not found with given review id.."));
	}

}
