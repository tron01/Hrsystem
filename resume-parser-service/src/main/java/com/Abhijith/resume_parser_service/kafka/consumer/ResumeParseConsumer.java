package com.Abhijith.resume_parser_service.kafka.consumer;

import com.Abhijith.resume_parser_service.kafka.event.ResumeParseEvent;
import com.Abhijith.resume_parser_service.model.ParsedResume;
import com.Abhijith.resume_parser_service.repository.ParsedResumeRepository;
import com.Abhijith.resume_parser_service.service.ResumeParserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ResumeParseConsumer {

	private final ResumeParserService parserService;
	private final ParsedResumeRepository resumeRepository;

	@KafkaListener(topics = "${kafka.topics.resume-parse}", groupId = "resume-parser-group")
	public void consume(ResumeParseEvent event) {
		log.info("Received resume parse event: {}", event);
		
		ParsedResume parsed = parserService.parseResumeFromPdfUrl(event);
		
		resumeRepository.save(parsed);
		
		log.info("Parsed and saved resume for applicationId: {}", event.getApplicationId());
		
		
	}
}