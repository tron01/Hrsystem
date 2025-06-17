package com.Abhijith.resume_parser_service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Slf4j
@Service
public class TikaTextExtractor {

	private final Tika tika = new Tika();
	
	public String extractTextFromPdfUrl(String pdfUrl) {
		
		try (InputStream inputStream = new URL(pdfUrl).openStream()) {
			return tika.parseToString(inputStream);
		} catch (TikaException te) {
			log.error("Tika parsing error: {}", te.getMessage(), te);
			return "";
		} catch (Exception e) {
			log.error("Error reading PDF from URL: {}", e.getMessage(), e);
			return "";
		}
	}
}