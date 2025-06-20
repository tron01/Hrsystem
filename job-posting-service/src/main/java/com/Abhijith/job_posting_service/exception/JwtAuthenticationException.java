package com.Abhijith.job_posting_service.exception;

public class JwtAuthenticationException extends RuntimeException {
	public JwtAuthenticationException(String message) {
		super(message);
	}
}
