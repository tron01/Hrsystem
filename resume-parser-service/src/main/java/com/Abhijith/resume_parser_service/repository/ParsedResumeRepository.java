package com.Abhijith.resume_parser_service.repository;

import com.Abhijith.resume_parser_service.model.ParsedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParsedResumeRepository extends MongoRepository<ParsedResume, String> {}
