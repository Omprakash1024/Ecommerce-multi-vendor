package com.app.oneplace.response;

import com.app.oneplace.domain.USER_ROLE;

import lombok.Data;

@Data
public class AuthResponse {

	private String jwt;
	private String message;
    private USER_ROLE role;
}
