package com.app.oneplace.model;

import com.app.oneplace.domain.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

	private String paymentId;
	private String razorpayPaymentLinkId;
	private String razorpayPaymnetLinkReferenceId;
	private String razonpayPaymentLinkStatus;
	private String razorpayPaymentIdZWSP;
	private PaymentStatus status;
}
