package com.Abhijith.application_submit_service.repository;

import com.Abhijith.application_submit_service.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

	Optional<Application> findByJobIdAndApplicantId(String jobId, String applicantId);
	
}

