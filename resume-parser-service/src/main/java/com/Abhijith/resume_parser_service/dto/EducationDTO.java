package com.Abhijith.resume_parser_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EducationDTO {
private String degree;
private String university;
private String startYear;
private String endYear;
private String CGPA;
}
