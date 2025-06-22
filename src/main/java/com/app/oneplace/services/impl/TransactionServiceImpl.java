package com.app.oneplace.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.Order;
import com.app.oneplace.model.Seller;
import com.app.oneplace.model.Transaction;
import com.app.oneplace.repo.SellerRepository;
import com.app.oneplace.repo.TransactionRepository;
import com.app.oneplace.services.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final SellerRepository sellerRepository;

	@Override
	public Transaction createTransaction(Order order) {
		Seller seller = sellerRepository.findById(order.getSellerId()).get();
		Transaction transaction = new Transaction();
		transaction.setSeller(seller);
		transaction.setCustomer(order.getUser());
		transaction.setOrder(order);

		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> getTransactionBySellerId(Seller seller) {

		return transactionRepository.findBySellerId(seller.getId());
	}

	@Override
	public List<Transaction> getAllTransaction() {

		return transactionRepository.findAll();
	}

}
