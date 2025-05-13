package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
