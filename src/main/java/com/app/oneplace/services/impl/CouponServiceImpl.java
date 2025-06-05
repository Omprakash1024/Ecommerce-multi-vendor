package com.app.oneplace.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.Coupon;
import com.app.oneplace.repo.CartRepository;
import com.app.oneplace.repo.CouponRepository;
import com.app.oneplace.repo.UserRepository;
import com.app.oneplace.services.CouponService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{
	
	private final CouponRepository couponRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	@Override
	public Cart applyCoupon(String code, double orderValue, AppUser user) throws Exception {
		Coupon coupon = couponRepository.findByCode(code);
		Cart cart= cartRepository.findByUserId(user.getId());
		
		if(coupon == null) {
			throw new Exception("Coupon not valid...");
		}
		if(user.getUsedCoupens().contains(coupon)) {
			throw new Exception("Coupon already used...");
		}
		if(orderValue< coupon.getMinimumOrderValue()) {
			throw new Exception("Coupon can't applied, minimum value of the order is "+ coupon.getMinimumOrderValue());
		}
		if(coupon.isActive() 
				&& LocalDate.now().isAfter(coupon.getValidityEndDate()) 
				&&  LocalDate.now().isBefore(coupon.getValidityEndDate())) {
			user.getUsedCoupens().add(coupon);
			userRepository.save(user);
			
			double discountPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
			cart.setBeforeCouponPrice(cart.getTotalSellingPrice());
			cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountPrice);
			cart.setCouponCode(code);
			cartRepository.save(cart);
			return cart;
		}
		throw new Exception("Coupon not valid...");
	}

	@Override
	public Cart removeCoupon(String code, AppUser user) throws Exception {
		
		Coupon coupon = couponRepository.findByCode(code);
		
		if(coupon == null) {
			throw new Exception("Coupon not found...");
		}
		
		Cart cart= cartRepository.findByUserId(user.getId());
		
		//double discountPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100; //12:46
		
		//cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountPrice);
		
		cart.setTotalSellingPrice(cart.getBeforeCouponPrice());
		cart.setCouponCode(null);
		user.getUsedCoupens().remove(coupon);
		
		userRepository.save(user);
		
		return cartRepository.save(cart);
		
	}

	@Override
	public Coupon findCouponById(Long id) throws Exception {
		
		return couponRepository.findById(id).orElseThrow(()-> new Exception("Coupon not found..."));
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public Coupon createCoupon(Coupon coupon) {
		
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findAllCoupons() {
		
		return couponRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public void deleteCoupon(Long id) throws Exception {
		Coupon coupon= findCouponById(id);
		couponRepository.delete(coupon);
		
	}

}
