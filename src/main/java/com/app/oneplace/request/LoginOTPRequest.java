package com.app.oneplace.request;

import com.app.oneplace.domain.USER_ROLE;

import lombok.Data;

@Data
public class LoginOTPRequest {

	private String email;
	private String otp;
	private USER_ROLE role;
}
