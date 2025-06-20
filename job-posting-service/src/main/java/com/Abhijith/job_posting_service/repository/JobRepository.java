package com.Abhijith.job_posting_service.repository;

import com.Abhijith.job_posting_service.dto.CompanyJobCount;
import com.Abhijith.job_posting_service.model.Job;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
	
	List<Job> findByPostedBy(String postedBy);

	@Aggregation(pipeline = {
			"{ $group: { _id: '$company', count: { $sum: 1 } } }"
	})
	List<CompanyJobCount> countJobsByCompany();
	
}

