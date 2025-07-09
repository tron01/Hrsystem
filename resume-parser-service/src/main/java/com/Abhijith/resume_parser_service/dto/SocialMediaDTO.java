package com.Abhijith.resume_parser_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialMediaDTO {
private String linkedin;
private String github;
private String portfolio;
}
