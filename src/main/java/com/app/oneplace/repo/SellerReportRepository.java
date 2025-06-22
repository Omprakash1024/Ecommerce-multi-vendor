package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.SellerReport;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {

	SellerReport findBySellerId(Long sellerId);
}
