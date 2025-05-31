package com.app.oneplace.services;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Wishlist;

public interface WishlistService {

	Wishlist createWishlist(AppUser user);
	Wishlist getWishlistByUserId(AppUser user);
	Wishlist addProductToWishlist(AppUser user, Product product);
	
}
