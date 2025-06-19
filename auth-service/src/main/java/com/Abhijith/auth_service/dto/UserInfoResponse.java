package com.Abhijith.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
	
	private String username;
	private String roles;
	
}
