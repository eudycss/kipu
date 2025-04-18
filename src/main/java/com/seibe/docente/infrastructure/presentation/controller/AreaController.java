package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.AreaDTO;
import com.seibe.docente.application.service.AreaService;
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
@RequestMapping("/api/areas")
@RequiredArgsConstructor
@Tag(name = "Áreas", description = "API para la gestión de áreas académicas")
public class AreaController {
    private final AreaService areaService;

    @GetMapping
    @Operation(summary = "Obtener todas las áreas")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RECTOR')")
    public ResponseEntity<List<AreaDTO>> findAll() {
        return ResponseEntity.ok(areaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un área por su ID")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RECTOR')")
    public ResponseEntity<AreaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(areaService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener un área por su nombre")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RECTOR')")
    public ResponseEntity<AreaDTO> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(areaService.findByNombre(nombre));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva área")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AreaDTO> create(@Valid @RequestBody AreaDTO areaDTO) {
        return new ResponseEntity<>(areaService.create(areaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un área existente")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AreaDTO> update(@PathVariable Long id, @Valid @RequestBody AreaDTO areaDTO) {
        return ResponseEntity.ok(areaService.update(id, areaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un área")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 