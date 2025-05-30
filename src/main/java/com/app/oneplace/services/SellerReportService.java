package com.app.oneplace.services;

import com.app.oneplace.model.Seller;
import com.app.oneplace.model.SellerReport;

public interface SellerReportService {

	SellerReport getSellerReport(Seller seller);
	SellerReport updateSellerReport(SellerReport sellerReport);
}
