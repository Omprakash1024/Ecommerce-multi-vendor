package com.app.oneplace.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponse {

	private String paymentLinkUrl;
	private String paymentLinkId;
}
