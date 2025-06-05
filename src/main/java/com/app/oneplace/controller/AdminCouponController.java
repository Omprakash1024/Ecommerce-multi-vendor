package com.app.oneplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.Coupon;
import com.app.oneplace.services.CartService;
import com.app.oneplace.services.CouponService;
import com.app.oneplace.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class AdminCouponController {

	private final CouponService couponService;
	private final UserService userService;
	
	public ResponseEntity<Cart> applyCoupon1(
			@RequestParam String apply,
			@RequestParam String code,
			@RequestParam double orderValue,
			@RequestHeader("Authorization") String jwt
			){
		
		
		return null;
	}
	
	@PostMapping("/apply")
	public ResponseEntity<Cart> applyCoupon(
			@RequestParam String apply,
			@RequestParam String code,
			@RequestParam double orderValue,
			@RequestHeader("Authorization") String jwt
			) throws Exception{
		AppUser user = userService.findUserByJWTtoken(jwt);
		Cart cart;
		
		if(apply.equals("true")) {
			cart = couponService.applyCoupon(code, orderValue, user);
			
		}else {
			cart = couponService.removeCoupon(code, user);
		}
		
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	
	@PostMapping("/admin/create")
	public ResponseEntity<Coupon> createCoupon(
			@RequestBody Coupon coupon){
		Coupon newCoupon = couponService.createCoupon(coupon);
		
		return new ResponseEntity<>(newCoupon,HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<?> deleteCoupon(
			@PathVariable Long couponId) throws Exception{
		couponService.deleteCoupon(couponId);
		
		return new ResponseEntity<>("Coupon deleted successfully",HttpStatus.OK);
	}
	
	@GetMapping("/admin/all")
	public ResponseEntity<List<Coupon>> getAllCoupons(){
		
		List<Coupon> newCoupon = couponService.findAllCoupons();
		
		return new ResponseEntity<>(newCoupon,HttpStatus.OK);
	}
}
