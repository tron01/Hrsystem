package com.Abhijith.resume_parser_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeParseEvent {
	private String jobId;
	private String applicationId;
	private String pdfUrl;
	private String eventType;
	private String correlationId;
	private Instant timestamp;
}