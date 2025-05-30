package com.app.oneplace.services;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.CartItem;

public interface CartItemService {

	CartItem updateCartItem(Long userId, Long Id, CartItem cartItem)throws Exception;
	void removeCartItem(Long userId, Long cartItemId) throws Exception;
	CartItem findCartItemById(Long id)throws Exception ;
}
