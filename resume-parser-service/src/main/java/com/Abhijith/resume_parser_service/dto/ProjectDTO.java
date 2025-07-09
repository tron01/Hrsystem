package com.Abhijith.resume_parser_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO {
private String title;
private String description;
private List<String> technologies;
}
