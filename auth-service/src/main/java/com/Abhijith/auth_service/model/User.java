package com.Abhijith.auth_service.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	private String id;
	private String username;
	private String password;
	private String role;
	private String email;
	private LocalDateTime createdAt;
	private Boolean enabled;
}
