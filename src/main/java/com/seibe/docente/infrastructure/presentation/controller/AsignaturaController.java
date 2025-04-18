package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.AsignaturaDTO;
import com.seibe.docente.application.service.AsignaturaService;
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
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
@Tag(name = "Asignaturas", description = "API para la gestión de asignaturas")
public class AsignaturaController {
    private final AsignaturaService asignaturaService;

    @GetMapping
    @Operation(summary = "Obtener todas las asignaturas")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<List<AsignaturaDTO>> findAll() {
        return ResponseEntity.ok(asignaturaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una asignatura por su ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<AsignaturaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.findById(id));
    }

    @GetMapping("/area/{areaId}")
    @Operation(summary = "Obtener asignaturas por área")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<List<AsignaturaDTO>> findByAreaId(@PathVariable Long areaId) {
        return ResponseEntity.ok(asignaturaService.findByAreaId(areaId));
    }

    @GetMapping("/area/{areaId}/nombre/{nombre}")
    @Operation(summary = "Obtener una asignatura por su nombre y área")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<AsignaturaDTO> findByNombreAndAreaId(
            @PathVariable String nombre,
            @PathVariable Long areaId) {
        return ResponseEntity.ok(asignaturaService.findByNombreAndAreaId(nombre, areaId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva asignatura")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AsignaturaDTO> create(@Valid @RequestBody AsignaturaDTO asignaturaDTO) {
        return new ResponseEntity<>(asignaturaService.create(asignaturaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una asignatura existente")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AsignaturaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AsignaturaDTO asignaturaDTO) {
        return ResponseEntity.ok(asignaturaService.update(id, asignaturaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una asignatura")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        asignaturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 