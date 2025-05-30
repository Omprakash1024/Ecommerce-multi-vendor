package com.app.oneplace.services.impl;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.Seller;
import com.app.oneplace.model.SellerReport;
import com.app.oneplace.repo.SellerReportRepository;
import com.app.oneplace.services.SellerReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService{
	
	private final SellerReportRepository sellerReportRepository;
	
	@Override
	public SellerReport getSellerReport(Seller seller) {
		SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());
		if(sellerReport==null) {
			SellerReport newReport = new SellerReport();
			newReport.setSeller(seller);
			return sellerReportRepository.save(newReport);
		}
		return sellerReport;
		
	}

	@Override
	public SellerReport updateSellerReport(SellerReport sellerReport) {
		return sellerReportRepository.save(sellerReport);
	}

	
}
