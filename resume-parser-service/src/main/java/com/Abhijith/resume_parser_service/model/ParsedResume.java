package com.Abhijith.resume_parser_service.model;


import com.Abhijith.resume_parser_service.dto.*;
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
	private String name;
	private String email;
	private String phone;
	private SocialMediaDTO socialMedia;
	private List<String> skills;
	private List<EducationDTO> education;
	private List<ExperienceDTO> experience;
	private List<ProjectDTO> projects;
	private List<LanguageDTO> languages;
	private List<CertificationDTO> certifications;
	
	// Metadata from event
	private String jobId;
	private String applicationId;
	private String pdfUrl;
	private Instant parsedAt;
	private String eventType;
	private String correlationId;
}
