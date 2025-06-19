package com.Abhijith.application_submit_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDto {

    private String id;
    private String jobId;
    private String applicantId;
    private String applicantName;
    private String email;
    private String phone;
    private String pdfUrl;
    private String status;
    private String appliedThrough;
    private LocalDateTime appliedAt;
    
}

