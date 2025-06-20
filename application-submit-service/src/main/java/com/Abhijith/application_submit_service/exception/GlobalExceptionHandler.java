package com.Abhijith.application_submit_service.exception;


import com.Abhijith.application_submit_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


	//jwt token related exceptions
	@ExceptionHandler(JwtAuthenticationException.class)
	public ResponseEntity<?> handleJwtException(JwtAuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ex.getMessage()));
	}


	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				       .body(Map.of("error", ex.getMessage()));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				       .body(Map.of("error", ex.getMessage()));
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> handleFileSizeLimit(MaxUploadSizeExceededException ex) {
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
				       .body(Map.of("error", "Uploaded file is too large."));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				       .body(Map.of("error", "Unexpected error.: " + ex.getMessage()));
		
	}
	
	
}