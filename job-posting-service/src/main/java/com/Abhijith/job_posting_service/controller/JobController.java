package com.Abhijith.job_posting_service.controller;

import com.Abhijith.job_posting_service.dto.JobDto;
import com.Abhijith.job_posting_service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public List<JobDto> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable String id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.createJob(jobDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDto> updateJob(@PathVariable String id, @RequestBody JobDto jobDto) {
        return jobService.updateJob(id, jobDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        return jobService.deleteJob(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

