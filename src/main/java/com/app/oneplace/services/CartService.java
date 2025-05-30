package com.app.oneplace.services;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Product;

public interface CartService {

	public CartItem addCartItem(
			AppUser user,
			Product product,
			String size,
			int quantity
			);
	
	public Cart findUserCart(AppUser user);
}
