package com.Abhijith.application_submit_service.repository;


import com.Abhijith.application_submit_service.model.ParsedResume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ParsedResumeRepository extends MongoRepository<ParsedResume, String> {

	Optional<ParsedResume> findByApplicationId(String applicationId);
	
}
