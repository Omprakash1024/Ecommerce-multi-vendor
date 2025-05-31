package com.app.oneplace.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findByProductId(Long productId);
	
}
