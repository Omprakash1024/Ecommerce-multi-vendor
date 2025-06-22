package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
