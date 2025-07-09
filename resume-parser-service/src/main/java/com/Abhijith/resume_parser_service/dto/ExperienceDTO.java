package com.Abhijith.resume_parser_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperienceDTO {
private String company;
private String position;
private String from;
private String to;
private String description;
}
