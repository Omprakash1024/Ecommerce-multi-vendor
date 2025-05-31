package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long>{

	Wishlist findByUserId(Long UserId);
}
