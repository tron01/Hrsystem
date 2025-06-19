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

  

   
    public ApplicationDto createApplication(String jobId, MultipartFile pdfFile) {
        String pdfUrl = cloudinaryService.uploadPdf(pdfFile);
        
        Application application = new Application();
        application.setJobId(jobId);
        application.setPdfUrl(pdfUrl);
        
        Application saved = applicationRepository.save(application);
        
        // 3. Convert to DTO
        ApplicationDto dto = new ApplicationDto();
        BeanUtils.copyProperties(saved, dto);
        
        sendResumeParseEvent(saved.getJobId(), saved.getId(), saved.getPdfUrl());
        
        return dto;
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



    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll().stream()
                       .map(application -> {
                           ApplicationDto dto = new ApplicationDto();
                           BeanUtils.copyProperties(application, dto);
                           return dto;
                       })
                       .collect(Collectors.toList());
    }

    public ApplicationDto getApplicationById(String id) {
        return applicationRepository.findById(id)
                       .map(application -> {
                           ApplicationDto dto = new ApplicationDto();
                           BeanUtils.copyProperties(application, dto);
                           return dto;
                       })
                       .orElse(null);
    }
    
}
