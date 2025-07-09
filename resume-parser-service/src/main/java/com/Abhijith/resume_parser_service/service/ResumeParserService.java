package com.Abhijith.resume_parser_service.service;

import com.Abhijith.resume_parser_service.dto.ResumeDataDTO;
import com.Abhijith.resume_parser_service.kafka.event.ResumeParseEvent;
import com.Abhijith.resume_parser_service.model.ParsedResume;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ExecutionException;


@Slf4j
@Service
@AllArgsConstructor
public class ResumeParserService {

	private final TikaTextExtractor textExtractor;
	private final OllamaParseResume ollamaParseResume;

	public ParsedResume parseResumeFromPdfUrl(ResumeParseEvent event) {
		
		log.info("Parsing PDF: {}", event.getPdfUrl());
		
		// Step 1: Extract text from PDF
		String content;
		try {
			content = textExtractor.extractTextFromPdfUrlAsync(event.getPdfUrl()).get();
			
			if (content == null || content.trim().isEmpty()) {
				log.warn("Extracted text is empty for URL: {}", event.getPdfUrl());
				throw new RuntimeException("Failed to extract text from PDF");
			}
			
		} catch (InterruptedException | ExecutionException e) {
			log.error("Failed to extract text from PDF", e);
			throw new RuntimeException("PDF extraction failed", e);
		}
		// Step 2: Parse resume data using LLM
		ResumeDataDTO parsedData;
		try {
			parsedData = ollamaParseResume.parse(content);
			log.info("Parsed resume data: {}", parsedData);
		} catch (Exception e) {
			log.error("LLM parsing failed", e);
			throw new RuntimeException("Failed to parse resume via LLM", e);
		}
		
		
		// Step 3: Map to ParsedResume and return
		return ParsedResume.builder()
				       .name(parsedData.getName())
				       .email(parsedData.getEmail())
				       .phone(parsedData.getPhone())
				       .socialMedia(parsedData.getSocialMedia())
				       .skills(parsedData.getSkills())
				       .education(parsedData.getEducation())
				       .experience(parsedData.getExperience())
				       .projects(parsedData.getProjects())
				       .languages(parsedData.getLanguages())
				       .certifications(parsedData.getCertifications())
				       .jobId(event.getJobId())
				       .applicationId(event.getApplicationId())
				       .pdfUrl(event.getPdfUrl())
				       .parsedAt(Instant.now())
				       .eventType(event.getEventType())
				       .correlationId(event.getCorrelationId())
				       .build();
	}
	
}
