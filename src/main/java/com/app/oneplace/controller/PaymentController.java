package com.app.oneplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.PaymentOrder;
import com.app.oneplace.model.Seller;
import com.app.oneplace.model.SellerReport;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.response.PaymentLinkResponse;
import com.app.oneplace.services.PaymentService;
import com.app.oneplace.services.SellerReportService;
import com.app.oneplace.services.SellerService;
import com.app.oneplace.services.TransactionService;
import com.app.oneplace.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final UserService userService;
	private final SellerService sellerService;
	private final SellerReportService reportService;
	private final TransactionService transactionService;
	
	
	@GetMapping("/{paymentId}")
	public ResponseEntity<ApiResponse> findUserCartHandler(
			@RequestHeader("Authorization") String jwt,
			@PathVariable String paymentId,
			@RequestParam String paymentLinkId
			) throws Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		
		PaymentLinkResponse paymentLinkResponse;
		
		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
		
		boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
		
		if(paymentSuccess) {
			for(Order order: paymentOrder.getOrders()) {
				transactionService.createTransaction(order);
				Seller seller= sellerService.getSellerById(order.getSellerId());
				SellerReport report= reportService.getSellerReport(seller);
				report.setTotalOrders(report.getTotalOrders()+1);
				report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
				report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
				reportService.updateSellerReport(report);
			}
		}
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Payment successful");
		 return new ResponseEntity<>(res, HttpStatus.CREATED);
		
	}
}
