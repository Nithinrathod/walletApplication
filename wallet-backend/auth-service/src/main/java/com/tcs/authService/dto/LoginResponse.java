package com.tcs.authService.dto;

import lombok.Data;

@Data
public class LoginResponse {
	private String userId;
	private String username;
	private String message;
	private String token; //

	// Constructor for Login (with token)
	public LoginResponse(String userId, String username, String message, String token) {
		this.userId = userId;
		this.username = username;
		this.message = message;
		this.token = token;
	}

	// Constructor for Register (no token)
	public LoginResponse(String userId, String username, String message) {
		this.userId = userId;
		this.username = username;
		this.message = message;
		this.token = null;
	}

	
}