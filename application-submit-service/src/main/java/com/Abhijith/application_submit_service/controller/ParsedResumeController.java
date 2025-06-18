package com.Abhijith.application_submit_service.controller;


import com.Abhijith.application_submit_service.dto.ParsedResumeDto;
import com.Abhijith.application_submit_service.service.ParsedResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications/parsed-resumes")
@RequiredArgsConstructor
public class ParsedResumeController {

    private final ParsedResumeService parsedResumeService;

    @GetMapping
    public List<ParsedResumeDto> getAllParsedResumes() {
        return parsedResumeService.getAllParsedResumes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParsedResumeDto> getParsedResumeById(@PathVariable String id) {
        return parsedResumeService.getParsedResumeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

