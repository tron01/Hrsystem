package com.Abhijith.application_submit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private String id;
    private String title;
    private String description;
    private String company;
    private String location;
    private Double salary;
    
    private String postedBy;             // User ID of the person who posted the job
    private String status;               // e.g., "Open", "Closed", "Pending"
    private String employmentType;       // e.g., "Full-time", "Internship"
    private String experienceLevel;      // e.g., "Entry", "Mid", "Senior"
    private List<String> skills;         // Required skills list
    private String educationRequirement; // e.g., "Bachelor's Degree"
    private String industry;             // e.g., "IT", "Finance"
    private LocalDateTime postedDate;
    private LocalDateTime closingDate;
    private boolean remote;
    private boolean active;
}

