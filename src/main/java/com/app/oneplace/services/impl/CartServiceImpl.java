package com.app.oneplace.services.impl;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Product;
import com.app.oneplace.repo.CartItemRepository;
import com.app.oneplace.repo.CartRepository;
import com.app.oneplace.services.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	
	@Override
	public CartItem addCartItem(AppUser user, Product product, String size, int quantity) {
		Cart cart = findUserCart(user);
		CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
		if(isPresent ==null) {
			CartItem cartItem =new CartItem();
			cartItem.setProduct(product);
		    cartItem.setQuantity(quantity);
		    cartItem.setUserId(user.getId());
		    cartItem.setSize(size);
		    
		    int totalPrice =quantity* product.getSellingPrice();
		    cartItem.setSellingPrice(totalPrice);
		    cartItem.setMrpPrice(quantity* product.getMrpPrice());
		    
		    cart.getCartItems().add(cartItem);
		    cartItem.setCart(cart);
		    
		    return cartItemRepository.save(cartItem);
		}
		return isPresent;
	}

	@Override
	public Cart findUserCart(AppUser user) {
		Cart cart = cartRepository.findByUserId(user.getId());
		int totalPrice =0;
		int totalDiscountPrice =0;
		int totalItem=0;
		for(CartItem item : cart.getCartItems()) {
			totalPrice+= item.getMrpPrice();
			totalDiscountPrice+=item.getSellingPrice();
			totalItem+= item.getQuantity();
		}
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalSellingPrice(totalDiscountPrice);
		cart.setDiscount(calcuateDiscountPercentage(totalPrice, totalDiscountPrice));
		return cart;
	}
	
	
	private int calcuateDiscountPercentage(int mrpPrice, int sellingPrice) {
		if(mrpPrice <=0) {
			return 0;
		}
		double discount = mrpPrice -sellingPrice;
		double discountPercentage = (discount/mrpPrice)*100;
		return (int) discountPercentage;
	}

}
