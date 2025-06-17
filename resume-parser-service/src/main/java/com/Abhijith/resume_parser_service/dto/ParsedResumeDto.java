package com.Abhijith.resume_parser_service.dto;

import lombok.*;
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

