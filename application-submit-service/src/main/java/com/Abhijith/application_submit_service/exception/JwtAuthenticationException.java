package com.Abhijith.application_submit_service.exception;

public class JwtAuthenticationException extends RuntimeException {
	public JwtAuthenticationException(String message) {
		super(message);
	}
}
