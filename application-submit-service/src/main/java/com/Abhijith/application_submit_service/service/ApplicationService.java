package com.Abhijith.application_submit_service.service;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.model.Application;
import com.Abhijith.application_submit_service.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CloudinaryService cloudinaryService;

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
        application.setId(null);
        return toDto(applicationRepository.save(application));
    }

    public Optional<ApplicationDto> updateApplication(String id, ApplicationDto dto) {
        return applicationRepository.findById(id).map(existing -> {
            BeanUtils.copyProperties(dto, existing, "id", "pdfUrl");
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
                .id(application.getId())
                .jobId(application.getJobId())
                .applicantName(application.getApplicantName())
                .email(application.getEmail())
                .phone(application.getPhone())
                .pdfUrl(application.getPdfUrl())
                .build();
    }

    private Application toEntity(ApplicationDto dto) {
        return Application.builder()
                .id(dto.getId())
                .jobId(dto.getJobId())
                .applicantName(dto.getApplicantName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .pdfUrl(dto.getPdfUrl())
                .build();
    }
}

