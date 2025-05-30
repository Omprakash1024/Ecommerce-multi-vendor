package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem,Long>{

	CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
