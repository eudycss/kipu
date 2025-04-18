package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.DocenteDTO;
import com.seibe.docente.application.service.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
@Tag(name = "Docentes", description = "API para la gestión de docentes")
public class DocenteController {
    private final DocenteService docenteService;

    @GetMapping
    @Operation(summary = "Obtener todos los docentes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<DocenteDTO>> findAll() {
        return ResponseEntity.ok(docenteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un docente por su ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<DocenteDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(docenteService.findById(id));
    }

    @GetMapping("/cedula/{cedula}")
    @Operation(summary = "Obtener un docente por su cédula")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<DocenteDTO> findByCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(docenteService.findByCedula(cedula));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo docente")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<DocenteDTO> create(@Valid @RequestBody DocenteDTO docenteDTO) {
        return new ResponseEntity<>(docenteService.create(docenteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "editar un docente existente")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<DocenteDTO> update(@PathVariable Long id, @Valid @RequestBody DocenteDTO docenteDTO) {
        return ResponseEntity.ok(docenteService.update(id, docenteDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un docente")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        docenteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/area/{areaId}")
    @Operation(summary = "Obtener docentes por área, con opción de ordenar todos los docentes priorizando el área especificada")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<DocenteDTO>> findByAreaId(
            @PathVariable Long areaId,
            @RequestParam(name = "mostrarTodos", defaultValue = "false") boolean mostrarTodos) {
        
        if (mostrarTodos) {
            return ResponseEntity.ok(docenteService.findAllOrderedByArea(areaId));
        } else {
            return ResponseEntity.ok(docenteService.findByAreaId(areaId));
        }
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de un docente (activar/desactivar)")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, Object>> cambiarEstado(@PathVariable Long id) {
        DocenteDTO docente = docenteService.findById(id);
        docente.setDocEstado(!docente.getDocEstado());  // Invertir el estado actual
        DocenteDTO actualizado = docenteService.update(id, docente);
        
        Map<String, Object> response = new HashMap<>();
        String nuevoEstado = actualizado.getDocEstado() ? "activo" : "inactivo";
        response.put("mensaje", "Estado del docente cambiado a: " + nuevoEstado);
        response.put("docente", actualizado);
        
        return ResponseEntity.ok(response);
    }
} 