package com.app.oneplace.services;

import java.util.List;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.Coupon;

public interface CouponService {

	Cart applyCoupon(String code, double orderValue, AppUser user) throws Exception ;
	
	Cart removeCoupon(String code,AppUser user)throws Exception ;
	
	Coupon findCouponById(Long id);
	
	Coupon createCoupon(Coupon coupon);
	
	List<Coupon> findAllCoupons();
	
	void deleteCoupon(Long id);
}
