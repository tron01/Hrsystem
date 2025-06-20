package com.Abhijith.application_submit_service.controller;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {
    
    private final ApplicationService applicationService;

    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDto>> getAllApplications() {
        List<ApplicationDto> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable String id) {
        ApplicationDto applicationDto = applicationService.getApplicationById(id);
        return applicationDto != null ? ResponseEntity.ok(applicationDto) : ResponseEntity.notFound().build();
    }

// --------------------------------logged in User jobs methods--------------------------------------------------------//


    @PostMapping(path = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApplicationDto> submitApplication(
            @RequestParam("jobId") String jobId,
            @RequestParam("applicantName") String applicantName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("resumeFile") MultipartFile resumeFile) {
        
        String applicantId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        log.info("------------User submitted application------------");
        log.info("Job ID = {}, Applicant = {} <{}>, FileName = {}", jobId, applicantName, email, resumeFile.getOriginalFilename());
        
        ApplicationDto createdApp = applicationService.createApplication(
                jobId, applicantId, applicantName, email, phone, resumeFile
        );
        
        return new ResponseEntity<>(createdApp, HttpStatus.CREATED);
    }


    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ApplicationDto>> getMyApplications() {
        String applicantId = getLoggedInUsername();
        return ResponseEntity.ok(applicationService.getApplicationsByApplicantId(applicantId));
    }
    
    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApplicationDto> getMyApplicationById(@PathVariable String id) {
        String applicantId = getLoggedInUsername();
        ApplicationDto dto = applicationService.getApplicationByIdAndApplicantId(id, applicantId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApplicationDto> updateMyApplication(
            @PathVariable String id,
            @RequestParam("phone") String phone,
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile) {
        String applicantId = getLoggedInUsername();
        ApplicationDto updated = applicationService.updateApplication(id, applicantId, phone, resumeFile);
        return ResponseEntity.ok(updated);
    }



// --------------------------------logged in HR jobs methods--------------------------------------------------------//

    @GetMapping("/hr")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<ApplicationDto>> getApplicationsForHr() {
        String hrUsername = getLoggedInUsername();
        return ResponseEntity.ok(applicationService.getApplicationsForHrByUsername(hrUsername));
    }
    
    @GetMapping("/hr/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApplicationDto> getApplicationForHrById(@PathVariable String id) {
        String hrUsername = getLoggedInUsername();
        ApplicationDto dto = applicationService.getApplicationByIdForHr(id, hrUsername);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    @PatchMapping("/hr/{id}/status")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApplicationDto> updateApplicationStatusByHr(
            @PathVariable String id,
            @RequestParam("status") String status) throws IllegalAccessException {
        
        String hrUsername = getLoggedInUsername();
        ApplicationDto updated = applicationService.updateApplicationStatusByHr(id, hrUsername, status);
        return ResponseEntity.ok(updated);
    }

    
    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // returns the logged-in username
    }
    
}

