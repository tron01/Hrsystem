package com.Abhijith.job_posting_service.controller;

import com.Abhijith.job_posting_service.dto.CreateJobDto;
import com.Abhijith.job_posting_service.dto.JobDto;
import com.Abhijith.job_posting_service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    
// --------------------------------logged in Users jobs methods--------------------------------------------------------//


    @GetMapping
    public List<JobDto> getAllJobsForUsers() {
        return jobService.getAllJobsForUser();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobByIdForUsers(@PathVariable String id) {
        return jobService.getJobById(id)
                       .map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

// --------------------------------logged in HR jobs methods--------------------------------------------------------//
    
    @PostMapping("/hr")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<JobDto> createJob(@RequestBody CreateJobDto createJobDto) {
        JobDto savedJob = jobService.createJob(createJobDto);
        return ResponseEntity.ok(savedJob);
    }
    @GetMapping("/hr")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<JobDto>> getMyJobs() {
        return ResponseEntity.ok(jobService.getAllJobsByCurrentUser());
    }
    
    @GetMapping("/hr/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<JobDto> getMyJobById(@PathVariable String id) {
        return jobService.getJobByIdForCurrentUser(id)
                       .map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/hr/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<JobDto> updateMyJob(@PathVariable String id, @RequestBody JobDto jobDto) {
        return jobService.updateJobByCurrentUser(id, jobDto)
                       .map(ResponseEntity::ok)
                       .orElse(ResponseEntity.status(403).build()); // Forbidden if not owner
    }
    
    @DeleteMapping("/hr/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deleteMyJob(@PathVariable String id) {
        boolean deleted = jobService.deleteJobByCurrentUser(id);
        return deleted
                       ? ResponseEntity.noContent().build()
                       : ResponseEntity.status(403).build(); // Forbidden if not owner
    }
    
}

