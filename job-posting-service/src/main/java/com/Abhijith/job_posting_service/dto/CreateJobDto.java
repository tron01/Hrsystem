package com.Abhijith.job_posting_service.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobDto {
	private String title;
	private String description;
	private String company;
	private String location;
	private Double salary;
	private String status;               // e.g., "Open"
	private String employmentType;       // e.g., "Full-time"
	private String experienceLevel;      // e.g., "Entry"
	private List<String> skills;
	private String educationRequirement;
	private String industry;
	private LocalDateTime closingDate;
	private boolean remote;
}
