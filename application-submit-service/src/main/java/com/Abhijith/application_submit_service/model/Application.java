package com.Abhijith.application_submit_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    
    private String postedBy;
    private String jobId;          // ID of the job being applied to
    private String applicantId;    // ID of the user/student applying
    private String applicantName;
    private String email;
    private String phone;
    private String pdfUrl;         // Cloudinary or storage link to resume
    
    private String status;         // e.g., "Submitted", "Under Review", "Shortlisted", "Rejected"
    private String appliedThrough; // e.g., "Portal", "Referral", "LinkedIn"
    private LocalDateTime appliedAt;
    private String notes;
}

