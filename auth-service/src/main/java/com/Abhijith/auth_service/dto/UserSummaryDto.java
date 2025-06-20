package com.Abhijith.auth_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UserSummaryDto {
private long totalUsers;
private Map<String, Long> userCountByRole;
}