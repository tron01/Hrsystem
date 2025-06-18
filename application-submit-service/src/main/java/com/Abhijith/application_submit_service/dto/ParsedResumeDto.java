package com.Abhijith.application_submit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedResumeDto {
    private String id;
    private String jobId;
    private String applicationId;
    private String pdfUrl;
    private Instant parsedAt;
    private String name;
    private String email;
    private String phone;
    private List<String> skills;
    private String experience;
    private String education;
}

