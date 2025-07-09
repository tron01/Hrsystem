package com.Abhijith.resume_parser_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ResumeParserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeParserServiceApplication.class, args);
	}

}
