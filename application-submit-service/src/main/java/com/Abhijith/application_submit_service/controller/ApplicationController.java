package com.Abhijith.application_submit_service.controller;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {
    
    private final ApplicationService applicationService;
    
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestParam("jobId") String jobId,
            @RequestParam("resumeFile") MultipartFile resumeFile) {
        
        log.info("------------Received request for uploading application------------");
        log.info(" JobId = {}, FileName = {}", jobId, resumeFile.getOriginalFilename());
        
        ApplicationDto createdApp = applicationService.createApplication(jobId,resumeFile);
        return new ResponseEntity<>(createdApp, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getAllApplications() {
        List<ApplicationDto> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable String id) {
        ApplicationDto applicationDto = applicationService.getApplicationById(id);
        if (applicationDto != null) {
            return ResponseEntity.ok(applicationDto);
        }
        return ResponseEntity.notFound().build();
    }
 
}

