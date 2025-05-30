package com.app.oneplace.services;

import java.util.Set;

import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.PaymentOrder;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

	PaymentOrder createOrder(AppUser user, Set<Order> orders);
	PaymentOrder getPaymentOrderById(Long orderId)  throws Exception ;
	PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception;
	Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
	PaymentLink createRazorpayPaymentLink(AppUser user, Long amount, Long OrderId)throws RazorpayException ;
	String createStripePaymentLink(AppUser user, Long amount, Long orderId) throws StripeException;
}
