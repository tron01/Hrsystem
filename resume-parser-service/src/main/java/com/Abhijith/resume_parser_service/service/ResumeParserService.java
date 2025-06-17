package com.Abhijith.resume_parser_service.service;

import com.Abhijith.resume_parser_service.kafka.event.ResumeParseEvent;
import com.Abhijith.resume_parser_service.model.ParsedResume;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import static com.Abhijith.resume_parser_service.util.ResumeTextExtractorUtils.*;

@Slf4j
@Service
@AllArgsConstructor
public class ResumeParserService {

	private final TikaTextExtractor textExtractor;
	
	public ParsedResume parseResumeFromPdfUrl(ResumeParseEvent event) {
		// TODO: Download and parse the PDF using Apache PDFBox, Tika, or NLP model
		log.info("Parsing PDF: {}", event.getPdfUrl());
		
		String content = textExtractor.extractTextFromPdfUrl(event.getPdfUrl());
		
		return ParsedResume.builder()
				       .jobId(event.getJobId())
				       .applicationId(event.getApplicationId())
				       .pdfUrl(event.getPdfUrl())
				       .parsedAt(Instant.now())
				       .name(extractName(content))
				       .email(extractEmail(content))
				       .phone(extractPhone(content))
				       .skills(extractSkills(content))
				       .experience(extractExperience(content))
				       .education(extractEducation(content))
				       .eventType(event.getEventType())
				       .correlationId(event.getCorrelationId())
				       .build();
	}

}
