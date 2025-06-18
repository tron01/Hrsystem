package com.Abhijith.application_submit_service.repository;


import com.Abhijith.application_submit_service.model.ParsedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParsedResumeRepository extends MongoRepository<ParsedResume, String> {}
