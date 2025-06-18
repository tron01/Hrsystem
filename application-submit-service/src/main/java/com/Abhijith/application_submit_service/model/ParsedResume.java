package com.Abhijith.application_submit_service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document("parsed_resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResume {
	@Id
	private String id;
	
	private String jobId;
	private String applicationId;
	private String pdfUrl;
	private Instant parsedAt;
	
	private String name;
	private String email;
	private String phone;
	private List<String> skills;
	private String experience;
	private String education;
	
	private String eventType;
	private String correlationId;
}
