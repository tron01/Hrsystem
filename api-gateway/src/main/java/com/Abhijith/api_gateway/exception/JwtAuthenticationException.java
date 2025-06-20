package com.Abhijith.api_gateway.exception;


public class JwtAuthenticationException extends RuntimeException {
public JwtAuthenticationException(String message) {
	super(message);
}
}
