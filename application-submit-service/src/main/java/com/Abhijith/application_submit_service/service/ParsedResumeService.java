package com.Abhijith.application_submit_service.service;


import com.Abhijith.application_submit_service.dto.ParsedResumeDto;
import com.Abhijith.application_submit_service.model.ParsedResume;
import com.Abhijith.application_submit_service.repository.ParsedResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParsedResumeService {

    private final ParsedResumeRepository parsedResumeRepository;

    public List<ParsedResumeDto> getAllParsedResumes() {
        return parsedResumeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ParsedResumeDto> getParsedResumeById(String id) {
        return parsedResumeRepository.findById(id).map(this::toDto);
    }

    public ParsedResume getParsedResumeByApplicationId(String applicationId) {
        return parsedResumeRepository.findByApplicationId(applicationId)
                       .orElseThrow(() -> new RuntimeException("Parsed resume not found for application ID: " + applicationId));
    }

    private ParsedResumeDto toDto(ParsedResume entity) {
        ParsedResumeDto dto = new ParsedResumeDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

