package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.PeriodoDTO;
import com.seibe.docente.application.service.PeriodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
@RequiredArgsConstructor
@Tag(name = "Periodos", description = "API para la gestión de periodos académicos")
public class PeriodoController {
    private final PeriodoService periodoService;

    @GetMapping
    @Operation(summary = "Obtener todos los periodos")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<PeriodoDTO>> findAll() {
        return ResponseEntity.ok(periodoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un periodo por su ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<PeriodoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(periodoService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener un periodo por su nombre")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<PeriodoDTO> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(periodoService.findByNombre(nombre));
    }

    @GetMapping("/activo")
    @Operation(summary = "Obtener el periodo activo actual")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<PeriodoDTO> findPeriodoActivo() {
        return ResponseEntity.ok(periodoService.findPeriodoActivo());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo periodo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PeriodoDTO> create(@Valid @RequestBody PeriodoDTO periodoDTO) {
        return new ResponseEntity<>(periodoService.create(periodoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un periodo existente")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PeriodoDTO> update(@PathVariable Long id, @Valid @RequestBody PeriodoDTO periodoDTO) {
        return ResponseEntity.ok(periodoService.update(id, periodoDTO));
    }

    @PutMapping("/{id}/activar")
    @Operation(summary = "Activar un periodo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PeriodoDTO> activarPeriodo(@PathVariable Long id) {
        return ResponseEntity.ok(periodoService.activarPeriodo(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un periodo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        periodoService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 