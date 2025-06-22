package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	Coupon findByCode(String code);
}
