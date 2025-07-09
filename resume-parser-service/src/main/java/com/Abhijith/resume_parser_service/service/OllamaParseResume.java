package com.Abhijith.resume_parser_service.service;

import com.Abhijith.resume_parser_service.dto.ResumeDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OllamaParseResume {
	
	private final ChatModel chatModel;

	public ResumeDataDTO parse(String input) throws JsonProcessingException {
		long start = System.currentTimeMillis();
		String response = chatModel.call(input);
		long end = System.currentTimeMillis();
		log.info("LLM Parsed resume in  {} ms", (end - start));
		
		// Optional: log raw response for debugging
		log.debug("LLM Raw Response: {}", response);
		
		// Extract JSON portion only
		String json = extractJsonBlock(response);
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, ResumeDataDTO.class);
	}


	private String extractJsonBlock(String text) {
		int start = text.indexOf('{');
		int end = text.lastIndexOf('}');
		if (start == -1 || end == -1 || end < start) {
			log.error("LLM response doesn't contain valid JSON object boundaries");
			throw new IllegalArgumentException("Invalid LLM response: no JSON object found");
		}
		return text.substring(start, end + 1);
	}
	
	
}
