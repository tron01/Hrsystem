package com.Abhijith.resume_parser_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeDataDTO {
private String name;
private String email;
private String phone;
private SocialMediaDTO socialMedia;
private List<String> skills;
private List<EducationDTO> education;
private List<ExperienceDTO> experience;
private List<ProjectDTO> projects;
private List<LanguageDTO> languages;
private List<CertificationDTO> certifications;
}
