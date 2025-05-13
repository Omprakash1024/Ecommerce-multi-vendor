package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long>{

	Seller findByEmail(String email);
}
