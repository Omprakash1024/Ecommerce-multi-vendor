package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{

	Coupon findByCode(String code);
}
