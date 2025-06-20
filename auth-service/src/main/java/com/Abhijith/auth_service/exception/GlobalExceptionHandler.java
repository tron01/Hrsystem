package com.Abhijith.auth_service.exception;

import com.Abhijith.auth_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	//jwt token related exceptions
	@ExceptionHandler(JwtAuthenticationException.class)
	public ResponseEntity<?> handleJwtException(JwtAuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(ex.getMessage()));
	}
	
	@ExceptionHandler({ BadCredentialsException.class, InternalAuthenticationServiceException.class })
	public ResponseEntity<ApiResponse> handleBadCredentials(Exception ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				       .body(new ApiResponse("Invalid username or password"));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMsg = ex.getBindingResult().getFieldErrors()
				                  .stream()
				                  .map(error -> error.getField() + ": " + error.getDefaultMessage())
				                  .collect(Collectors.joining(", "));
		return ResponseEntity.badRequest()
				       .body(new ApiResponse(errorMsg));
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
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponse> handleNotFoundException(NoResourceFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				       .body(new ApiResponse("Resource not found: " + ex.getMessage()));
	}
	
}
