package com.Abhijith.auth_service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {

	private String id;
	private String username;
	private String role;
	private String email;
	private LocalDateTime createdAt;
	private Boolean enabled;

}
