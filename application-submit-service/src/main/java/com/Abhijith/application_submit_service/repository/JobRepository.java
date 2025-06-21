package com.Abhijith.application_submit_service.repository;


import com.Abhijith.application_submit_service.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface JobRepository extends MongoRepository<Job, String> {
	Optional<Job> findByIdAndActiveTrue(String jobId);
	
}

