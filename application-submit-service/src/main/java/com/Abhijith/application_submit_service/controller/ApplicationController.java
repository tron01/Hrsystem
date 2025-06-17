package com.Abhijith.application_submit_service.controller;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public List<ApplicationDto> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable String id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestPart("application") ApplicationDto applicationDto,
            @RequestPart("pdfFile") MultipartFile pdfFile) {
        return ResponseEntity.ok(applicationService.createApplication(applicationDto, pdfFile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationDto> updateApplication(@PathVariable String id, @RequestBody ApplicationDto applicationDto) {
        return applicationService.updateApplication(id, applicationDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        return applicationService.deleteApplication(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

