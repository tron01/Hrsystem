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
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDto> updateJob(@PathVariable String id, @RequestBody JobDto jobDto) {
        return jobService.updateJob(id, jobDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        return jobService.deleteJob(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
// --------------------------------logged in Users jobs methods--------------------------------------------------------//


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<JobDto> getAllJobs() {
        return jobService.getAllJobs();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JobDto> getJobById(@PathVariable String id) {
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

