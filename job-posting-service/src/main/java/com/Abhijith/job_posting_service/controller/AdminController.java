package com.Abhijith.job_posting_service.controller;

import com.Abhijith.job_posting_service.dto.ErrorResponse;
import com.Abhijith.job_posting_service.dto.JobDto;
import com.Abhijith.job_posting_service.service.AdminJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
public class AdminController {

	private final AdminJobService jobService;

	@GetMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public List<JobDto> getAllJobs() {
		return jobService.getAllJobs();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<JobDto> getJobById(@PathVariable String id) {
		return jobService.getJobById(id)
				       .map(ResponseEntity::ok)
				       .orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateJob(@PathVariable String id, @RequestBody JobDto jobDto) {
		Optional<JobDto> updated = jobService.updateJob(id, jobDto);
		
		if (updated.isPresent()) {
			return ResponseEntity.ok(updated.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					       .body(new ErrorResponse("Job not found with id: " + id, 404, LocalDateTime.now()));
		}
	}

	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteJob(@PathVariable String id) {
		boolean deleted = jobService.deleteJob(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					       .body(new ErrorResponse("Job not found with id: " + id, 404, LocalDateTime.now()));
		}
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/count")
	public ResponseEntity<Map<String, Long>> getTotalJobCount() {
		Map<String, Long> response = Map.of("totalJobs", jobService.getTotalJobCount());
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/count-by-company")
	public ResponseEntity<List<Map<String, Object>>> getJobCountByCompany() {
		return ResponseEntity.ok(jobService.getJobCountByCompany());
	}
	
}
