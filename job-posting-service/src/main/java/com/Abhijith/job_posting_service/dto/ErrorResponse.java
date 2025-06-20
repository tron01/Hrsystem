package com.Abhijith.job_posting_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
	private String message;
	private int status;
	private LocalDateTime timestamp;
}
