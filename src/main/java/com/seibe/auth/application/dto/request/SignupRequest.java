package com.seibe.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * DTO para solicitudes de registro de usuarios
 */
@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    
    @NotBlank
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
} 