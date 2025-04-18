package com.seibe.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para solicitudes de inicio de sesión
 */
@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;
} 