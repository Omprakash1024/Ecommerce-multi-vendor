package com.app.oneplace.services.impl;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Wishlist;
import com.app.oneplace.repo.WishlistRepository;
import com.app.oneplace.services.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
	
	private final WishlistRepository wishlistRepository;
	
	@Override
	public Wishlist createWishlist(AppUser user) {
		Wishlist wishlist=new Wishlist();
		wishlist.setUser(user);
		return wishlistRepository.save(wishlist);
	}

	@Override
	public Wishlist getWishlistByUserId(AppUser user) {
		Wishlist wishlist = wishlistRepository.findByUserId(user.getId());
		if(wishlist==null) {
			wishlist = createWishlist(user);
		}
		return wishlist;
	}

	@Override
	public Wishlist addProductToWishlist(AppUser user, Product product) {
		Wishlist wishlist = getWishlistByUserId(user);
		
		if(wishlist.getProducts().contains(product)) {
			wishlist.getProducts().remove(product);
		}else {
			wishlist.getProducts().add(product);
		}
		return wishlistRepository.save(wishlist);
	}

}
