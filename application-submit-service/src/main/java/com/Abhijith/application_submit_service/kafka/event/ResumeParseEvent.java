package com.Abhijith.application_submit_service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeParseEvent {
	private String jobId;
	private String applicationId;
	private String pdfUrl;
	private String eventType;       // e.g., "RESUME_PARSE_REQUEST"
	private String correlationId;   // e.g., UUID
	private Instant timestamp;      // ISO-8601 format time
}