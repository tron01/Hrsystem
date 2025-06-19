package com.Abhijith.job_posting_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "jobs")
public class Job {
    @Id
    private String id;
    private String title;
    private String description;
    private String company;
    private String location;
    private Double salary;
    private String postedBy;         // User ID of the person who posted the job
    private String status;// e.g., "Open", "Closed", "Pending"
    private String employmentType;     // e.g., "Full-time", "Part-time", "Internship", "Contract"
    private String experienceLevel;    // e.g., "Entry", "Mid", "Senior", "Lead"
    private List<String> skills;       // List of required skills/technologies
    private String educationRequirement; // e.g., "Bachelor's in Computer Science"
    private String industry;           // e.g., "IT", "Healthcare", "Finance"
    private LocalDateTime postedDate;
    private LocalDateTime closingDate;
    private boolean remote;          // Whether remote work is allowed
    private boolean active;          // Whether the job is still accepting applications
}

