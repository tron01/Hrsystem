package com.Abhijith.application_submit_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDto {
    private String id;
    private String jobId;
    private String applicantName;
    private String email;
    private String phone;
    private String pdfUrl;
}

