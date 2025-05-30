package com.app.oneplace.services;

import java.util.List;

import com.app.oneplace.domain.AccountStatus;
import com.app.oneplace.exceptions.SellerException;
import com.app.oneplace.model.Seller;

public interface SellerService {

	Seller getSellerProfile(String jwt) throws Exception;
	Seller createSeller(Seller seller) throws Exception;
	Seller getSellerById(Long id) throws SellerException;
	Seller getSellerByEmail(String email) throws Exception;
	List<Seller> getAllSellers(AccountStatus status);
	Seller updateSeller(Long id, Seller seller) throws SellerException;
	void deleteSeller(Long id) throws SellerException;
	Seller verifyEmail(String email, String otp) throws Exception;
	Seller updateSellerAccountStatus(Long id, AccountStatus status) throws SellerException;
}
