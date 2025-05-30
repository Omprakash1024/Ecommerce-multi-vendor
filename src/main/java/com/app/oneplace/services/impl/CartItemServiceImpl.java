package com.app.oneplace.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.repo.CartItemRepository;
import com.app.oneplace.services.CartItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{

	private final CartItemRepository cartItemRepository;
	
	@Override
	public CartItem updateCartItem(Long userId, Long Id, CartItem cartItem) throws Exception {
		CartItem item = findCartItemById(Id);
		AppUser cartItemUser = item.getCart().getUser();
		
		if(cartItemUser.getId().equals(userId)) {
			//item.setQuantity(item.getQuantity()+cartItem.getQuantity()); //doubt
			
			item.setQuantity(cartItem.getQuantity()); 
			item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
			item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
			return cartItemRepository.save(item);
		}
		
		throw new Exception("You can't update this cart items");
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws Exception {
		CartItem item = findCartItemById(cartItemId);
		AppUser cartItemUser = item.getCart().getUser();
		
		if(cartItemUser.getId().equals(userId)) {
			cartItemRepository.delete(item);
		}
		else {
			throw new Exception("You cant delete this cart item");
		}
	}

	@Override
	public CartItem findCartItemById(Long id) throws Exception {
		Optional<CartItem> item =cartItemRepository.findById(id);
		if(item.isPresent()) {
			return item.get();
		}
		throw new Exception("Cart item not found with the ID - "+id);
	}

}
