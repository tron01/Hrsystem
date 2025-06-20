package com.Abhijith.job_posting_service.exception;

import com.Abhijith.job_posting_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

	//jwt token related exceptions
	@ExceptionHandler(JwtAuthenticationException.class)
	public ResponseEntity<?> handleJwtException(JwtAuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				       .body(new ApiResponse( "Something went wrong: " + ex.getMessage()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(
				new ApiResponse("Invalid JSON format: " + ex.getMostSpecificCause().getMessage())
		);
	}
	
}
