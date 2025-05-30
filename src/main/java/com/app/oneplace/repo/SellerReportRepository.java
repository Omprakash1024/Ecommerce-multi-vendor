package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long>{

	SellerReport findBySellerId(Long sellerId);
}
