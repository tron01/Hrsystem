package com.Abhijith.resume_parser_service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TikaTextExtractor {

	private final Tika tika = new Tika();

	@Async
	public CompletableFuture<String> extractTextFromPdfUrlAsync(String pdfUrl) {
		try (InputStream inputStream = new URL(pdfUrl).openStream()) {
			return CompletableFuture.completedFuture(tika.parseToString(inputStream));
		} catch (Exception e) {
			log.error("Error reading PDF: {}", e.getMessage(), e);
			return CompletableFuture.completedFuture("");
		}
	}
}