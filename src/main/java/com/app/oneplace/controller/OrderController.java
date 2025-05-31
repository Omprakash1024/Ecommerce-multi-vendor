package com.app.oneplace.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.domain.PaymentMethod;
import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.Address;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.OrderItem;
import com.app.oneplace.model.PaymentOrder;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Seller;
import com.app.oneplace.model.SellerReport;
import com.app.oneplace.repo.OrderItemRepository;
import com.app.oneplace.repo.PaymentOrderRepository;
import com.app.oneplace.request.AddItemRequest;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.response.PaymentLinkResponse;
import com.app.oneplace.services.CartService;
import com.app.oneplace.services.OrderService;
import com.app.oneplace.services.PaymentService;
import com.app.oneplace.services.SellerReportService;
import com.app.oneplace.services.SellerService;
import com.app.oneplace.services.UserService;
import com.razorpay.PaymentLink;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {

	private final OrderService orderService;
	private final OrderItemRepository orderItemRepository;
	private final UserService userService;
	private final CartService cartService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService; 
	private final PaymentService paymentService;
	private final PaymentOrderRepository paymentOrderRepository;    
	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
			@RequestBody Address shippingAddress,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization") String jwt) throws  Exception{
		
		
		
	     AppUser user = userService.findUserByJWTtoken(jwt);
	     
	     Cart  cart = cartService.findUserCart(user);
	     
	     Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
	     
	    // 
		// payment implementation
	     PaymentOrder paymentOrder = paymentService.createOrder(user, orders);
	     
	     PaymentLinkResponse response = new PaymentLinkResponse(); 
	     
		if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
			
			PaymentLink payment = paymentService.createRazorpayPaymentLink(
					user, 
					paymentOrder.getAmount(), 
					paymentOrder.getId());
			
			String paymentUrl = payment.get("short_url");
			String paymentUrlId = payment.get("id");
			
			response.setPaymentLinkUrl(paymentUrl);
			paymentOrder.setPaymentLinkId(paymentUrl);
			paymentOrderRepository.save(paymentOrder);
			
			
		}else {
			String paymenturl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			
			response.setPaymentLinkUrl(paymenturl);
			paymentOrder.setPaymentLinkId(paymenturl);
			paymentOrderRepository.save(paymentOrder);
		}
		 return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Order>> userOrderHistoryHandler(
			@RequestHeader("Authorization") String jwt) throws  Exception{
	
		AppUser user = userService.findUserByJWTtoken(jwt);
		List<Order> orders = orderService.userOrderHistory(user.getId());
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(
			@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws  Exception{
	
		AppUser user = userService.findUserByJWTtoken(jwt);
		Order order = orderService.findOrderById(user.getId());
		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(
			@PathVariable Long orderItemId,
			@RequestHeader("Authorization") String jwt) throws  Exception{
	
		AppUser user = userService.findUserByJWTtoken(jwt);
		OrderItem order = orderService.findOrderItemById(user.getId());
		return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<Order> cancelOrder(
			@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws  Exception{
	
		AppUser user = userService.findUserByJWTtoken(jwt);
		Order order = orderService.cancelOrder(orderId,user);
	
		Seller seller = sellerService.getSellerById(order.getSellerId());
		
		SellerReport report = sellerReportService.getSellerReport(seller);
		
		report.setCancelledOrders(report.getCancelledOrders()+1);
		report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
		
		sellerReportService.updateSellerReport(report);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	
}
