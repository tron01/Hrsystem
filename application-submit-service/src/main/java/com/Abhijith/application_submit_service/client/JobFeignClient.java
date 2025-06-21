package com.Abhijith.application_submit_service.client;

import com.Abhijith.application_submit_service.dto.JobDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-posting-service", path = "/api/jobs")
public interface JobFeignClient {
	@GetMapping("/{id}")
	JobDto getJobByIdForUsers(@PathVariable("id") String id);
}
