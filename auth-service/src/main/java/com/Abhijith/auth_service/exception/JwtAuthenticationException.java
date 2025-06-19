package com.Abhijith.auth_service.exception;

public class JwtAuthenticationException extends RuntimeException {
	public JwtAuthenticationException(String message) {
		super(message);
	}
}
