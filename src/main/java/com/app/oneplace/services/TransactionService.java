package com.app.oneplace.services;

import java.util.List;

import com.app.oneplace.model.Order;
import com.app.oneplace.model.Seller;
import com.app.oneplace.model.Transaction;

public interface TransactionService {

	Transaction createTransaction(Order order);
	List<Transaction> getTransactionBySellerId(Seller seller);
	List<Transaction> getAllTransaction();
}
