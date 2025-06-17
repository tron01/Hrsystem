package com.Abhijith.application_submit_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    private String jobId;
    private String applicantName;
    private String email;
    private String phone;
    private String pdfUrl;
}

