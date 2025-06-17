package com.Abhijith.application_submit_service.service;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.kafka.event.ResumeParseEvent;
import com.Abhijith.application_submit_service.kafka.producer.ResumeParseProducer;
import com.Abhijith.application_submit_service.model.Application;
import com.Abhijith.application_submit_service.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CloudinaryService cloudinaryService;
    private final ResumeParseProducer resumeParseProducer;

    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ApplicationDto> getApplicationById(String id) {
        return applicationRepository.findById(id).map(this::toDto);
    }

    public ApplicationDto createApplication(ApplicationDto dto, MultipartFile pdfFile) {
        String pdfUrl = cloudinaryService.uploadPdf(pdfFile);
        Application application = toEntity(dto);
        application.setPdfUrl(pdfUrl);
        Application saved = applicationRepository.save(application);
        
        // Send message to Kafka for resume parsing
        sendResumeParseEvent(saved.getJobId(), saved.getId(), saved.getPdfUrl());
        
        return toDto(saved);
    }

    private void sendResumeParseEvent(String jobId, String applicationId, String pdfUrl) {
        
        ResumeParseEvent event = new ResumeParseEvent(
                jobId,
                applicationId,
                pdfUrl,
                "RESUME_PARSE_REQUEST",
                UUID.randomUUID().toString(),
                Instant.now()
        );
        
        resumeParseProducer.sendResumeParseEvent(event);
    }

    public Optional<ApplicationDto> updateApplication(String id, ApplicationDto dto) {
        return applicationRepository.findById(id).map(existing -> {
            BeanUtils.copyProperties(dto, existing,"pdfUrl","jobId");
            return toDto(applicationRepository.save(existing));
        });
    }

    public boolean deleteApplication(String id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ApplicationDto toDto(Application application) {
        return ApplicationDto.builder()
                .jobId(application.getJobId())
                .applicantName(application.getApplicantName())
                .email(application.getEmail())
                .phone(application.getPhone())
                .pdfUrl(application.getPdfUrl())
                .build();
    }

    private Application toEntity(ApplicationDto dto) {
        return Application.builder()
                .jobId(dto.getJobId())
                .applicantName(dto.getApplicantName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .pdfUrl(dto.getPdfUrl())
                .build();
    }
}
