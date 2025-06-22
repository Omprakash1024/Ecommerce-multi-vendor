package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByUserId(Long id);
}
