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
import java.time.LocalDateTime;
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

    
    public ApplicationDto createApplication(String jobId,
                                        String applicantId,
                                        String applicantName,
                                        String email,
                                        String phone,
                                        MultipartFile pdfFile) {
        // Validate file content type
        if (!"application/pdf".equalsIgnoreCase(pdfFile.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }
        
        // Check for existing application
        Optional<Application> existing = applicationRepository.findByJobIdAndApplicantId(jobId, applicantId);
        if (existing.isPresent()) {
            throw new IllegalStateException("You have already applied for this job.");
        }
        
        String pdfUrl = cloudinaryService.uploadPdf(pdfFile);
        
        Application application = Application.builder()
                                          .jobId(jobId)
                                          .applicantId(applicantId)
                                          .applicantName(applicantName)
                                          .email(email)
                                          .phone(phone)
                                          .pdfUrl(pdfUrl)
                                          .status("Submitted")
                                          .appliedThrough("Portal")
                                          .appliedAt(LocalDateTime.now())
                                          .build();
        
        Application saved = applicationRepository.save(application);
        
        //call event trigger to parse resume
        sendResumeParseEvent(saved.getJobId(), saved.getId(), saved.getPdfUrl());
        
        return toDto(saved);
    }

    //event to trigger resume parsing
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


    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll().stream()
                       .map(this::toDto)
                       .collect(Collectors.toList());
    }
    
    public ApplicationDto getApplicationById(String id) {
        return applicationRepository.findById(id)
                       .map(this::toDto)
                       .orElse(null);
    }


    private ApplicationDto toDto(Application application) {
        ApplicationDto dto = new ApplicationDto();
        BeanUtils.copyProperties(application, dto);
        return dto;
    }
    
}
