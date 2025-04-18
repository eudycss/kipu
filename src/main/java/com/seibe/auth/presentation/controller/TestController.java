package com.seibe.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing access control
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Endpoints para pruebas de acceso")
public class TestController {
    
    @GetMapping("/all")
    @Operation(summary = "Contenido público accesible para todos los usuarios")
    public String allAccess() {
        return "Contenido público.";
    }
    
    @GetMapping("/docente")
    @PreAuthorize("hasRole('ROLE_DOCENTE')")
    @Operation(summary = "Contenido exclusivo para docentes", security = @SecurityRequirement(name = "bearerAuth"))
    public String docenteAccess() {
        return "Contenido para DOCENTES";
    }

    @GetMapping("/rector")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    @Operation(summary = "Contenido exclusivo para rectores", security = @SecurityRequirement(name = "bearerAuth"))
    public String rectorAccess() {
        return "Contenido solo para RECTORES.";
    }
} 