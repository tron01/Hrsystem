package com.Abhijith.application_submit_service.repository;

import com.Abhijith.application_submit_service.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

	List<Application> findByApplicantId(String applicantId);
	Optional<Application> findByIdAndApplicantId(String id, String applicantId);
	List<Application> findByPostedBy(String hrId);
	boolean existsByJobIdAndApplicantId(String jobId, String applicantId);
	
}

