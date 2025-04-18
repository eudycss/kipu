package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.AsignaturaResponseDTO;
import com.seibe.docente.application.dto.DocenteDisponibilidadDTO;
import com.seibe.docente.application.dto.OfertaDTO;
import com.seibe.docente.application.service.OfertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
@RequiredArgsConstructor
@Tag(name = "Ofertas", description = "API para la gestión de ofertas académicas")
public class OfertaController {
    private final OfertaService ofertaService;

    @GetMapping("/proceso/{procesoId}")
    @Operation(summary = "Obtener ofertas por proceso educativo y periodo")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<OfertaDTO>> findByProcesoAndPeriodo(
            @PathVariable Long procesoId,
            @RequestParam Long periodoId) {
        return ResponseEntity.ok(ofertaService.findByProcesoAndPeriodo(procesoId, periodoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una oferta por su ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<OfertaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.findById(id));
    }
    
    @GetMapping("/{id}/docentes")
    @Operation(summary = "Obtener lista de docentes disponibles y no disponibles para una oferta")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<DocenteDisponibilidadDTO>> findDocentesByOfertaId(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.findDocentesByOfertaId(id));
    }
    
    @GetMapping("/{id}/asignaturas")
    @Operation(summary = "Obtener lista de asignaturas asociadas a una oferta")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<List<AsignaturaResponseDTO>> findAsignaturasByOfertaId(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.findAsignaturasByOfertaId(id));
    }
} 