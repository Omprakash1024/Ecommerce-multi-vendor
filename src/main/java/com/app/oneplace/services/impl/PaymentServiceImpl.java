package com.app.oneplace.services.impl;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.app.oneplace.domain.PaymentOrderStatus;
import com.app.oneplace.domain.PaymentStatus;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.PaymentOrder;
import com.app.oneplace.repo.OrderRepository;
import com.app.oneplace.repo.PaymentOrderRepository;
import com.app.oneplace.services.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.Mode;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	
	private final PaymentOrderRepository paymentOrderRepository;
	private final OrderRepository orderRepository;
	
	
	//razorpay business account's apikey and apisecret should provide below
	private String apiKey ="apikey";
	private String apiSecret="apisecret";
	
	//stripe secret key
	private String stripeSecretKey="stripesecretkey";
	
	@Override
	public PaymentOrder createOrder(AppUser user, Set<Order> orders) {
		Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
		
		PaymentOrder order= new PaymentOrder();
		order.setAmount(amount);
		order.setUser(user);
		order.setOrders(orders);
		
		return paymentOrderRepository.save(order);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
		
		return paymentOrderRepository.findById(orderId).orElseThrow(()-> new Exception("Payment order not found..."));
	}

	@Override
	public PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception {
		PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
		
		if(paymentOrder ==null) {
			throw new Exception("Payment order not found with provided payment Link id...");
		}
		return null;
	}

	@Override
	public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
		
		if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			
			RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
			Payment payment = razorpay.payments.fetch(paymentId);
			
			String status = payment.get("status");
			
			if(status.equals("captured")) {
				Set<Order> orders = paymentOrder.getOrders();
				
				for(Order order: orders) {
					order.setPaymentStatus(PaymentStatus.COMPLETED);
					orderRepository.save(order);
				}
				paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
				paymentOrderRepository.save(paymentOrder);
				return true;
			}
			paymentOrder.setStatus(PaymentOrderStatus.FAILED);
			paymentOrderRepository.save(paymentOrder);
			return false;
		}
		return false;
	}

	@Override
	public PaymentLink createRazorpayPaymentLink(AppUser user, Long amount, Long orderId) throws RazorpayException {
		amount = amount*100;
		try {
			
			RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", amount);
			paymentLinkRequest.put("currency","INR");
			
			JSONObject customer = new JSONObject();
			customer.put("name",user.getUsername());
			customer.put("email",user.getEmail());
			
			paymentLinkRequest.put("customer", customer);
			
			//notify
			
			JSONObject notify = new JSONObject();
			notify.put("email",true);
	    	//	notify.put("sms",true);
			paymentLinkRequest.put("notify", notify);
			
			//frontend url
			paymentLinkRequest.put("callback_url","http://localhost:8080/payment/success/"+orderId);
			
			paymentLinkRequest.put("callback_method", "get");
			
			//payment link 
			
			PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkUrl= paymentLink.get("short_url");
			String paymentLinkId= paymentLink.get("id");
			
			return paymentLink;
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw new RazorpayException(e.getMessage());
		}
	}

	@Override
	public String createStripePaymentLink(AppUser user, Long amount, Long orderId) throws StripeException {
		Stripe.apiKey=stripeSecretKey;
		SessionCreateParams params = SessionCreateParams.
				builder().
				addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8080/payment/success/"+orderId)
				.setCancelUrl("http://localhost:8080/payment/cancel/")
				.addLineItem(
						 LineItem.builder()
						.setQuantity(1L)
						.setPriceData(LineItem.PriceData.builder()
								.setCurrency("usd")
								.setUnitAmount(amount*100)
								.setProductData(
										LineItem.PriceData.ProductData
										.builder().setName("OnePlace payment")
										.build()
									).build()
								).build()
						).build();
		
		Session session = Session.create(params);
		
		
		return session.getUrl();
	}

}
