package com.Abhijith.resume_parser_service.service;

import com.Abhijith.resume_parser_service.dto.ParsedResumeDto;
import com.Abhijith.resume_parser_service.model.ParsedResume;
import com.Abhijith.resume_parser_service.repository.ParsedResumeRepository;
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

    private ParsedResumeDto toDto(ParsedResume entity) {
        ParsedResumeDto dto = new ParsedResumeDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

