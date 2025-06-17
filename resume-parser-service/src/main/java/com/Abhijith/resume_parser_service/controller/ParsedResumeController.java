package com.Abhijith.resume_parser_service.controller;

import com.Abhijith.resume_parser_service.dto.ParsedResumeDto;
import com.Abhijith.resume_parser_service.service.ParsedResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parsed-resumes")
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

