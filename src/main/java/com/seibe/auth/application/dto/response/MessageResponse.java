package com.seibe.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas simples con mensaje
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
} 