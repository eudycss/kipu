package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.GradoDTO;
import com.seibe.docente.application.service.GradoService;
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
@RequestMapping("/api/grados")
@RequiredArgsConstructor
@Tag(name = "Grados", description = "API para la gestión de grados académicos")
public class GradoController {
    private final GradoService gradoService;

    @GetMapping
    @Operation(summary = "Obtener todos los grados")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<List<GradoDTO>> findAll() {
        return ResponseEntity.ok(gradoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un grado por su ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<GradoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(gradoService.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener un grado por su nombre")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<GradoDTO> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(gradoService.findByNombre(nombre));
    }

    @GetMapping("/nivel/{nivel}")
    @Operation(summary = "Obtener grados por nivel")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR') or hasRole('ROLE_DOCENTE')")
    public ResponseEntity<List<GradoDTO>> findByNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(gradoService.findByNivel(nivel));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo grado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GradoDTO> create(@Valid @RequestBody GradoDTO gradoDTO) {
        return new ResponseEntity<>(gradoService.create(gradoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un grado existente")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GradoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody GradoDTO gradoDTO) {
        return ResponseEntity.ok(gradoService.update(id, gradoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un grado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradoService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 