package com.Abhijith.job_posting_service.dto;

import lombok.*;

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
}

