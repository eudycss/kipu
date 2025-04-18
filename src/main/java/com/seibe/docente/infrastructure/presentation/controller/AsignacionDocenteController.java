package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.AsignacionDocenteDTO;
import com.seibe.docente.application.dto.AsignacionDocenteListResponse;
import com.seibe.docente.application.dto.OfertaProcesoDTO;
import com.seibe.docente.application.service.AsignacionDocenteService;
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
@RequestMapping("/api/asignaciones-docente")
@RequiredArgsConstructor
public class AsignacionDocenteController {

    private final AsignacionDocenteService asignacionDocenteService;

    @GetMapping("/ofertas")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<OfertaProcesoDTO>> getOfertasPorProceso(
            @RequestParam Long procesoId,
            @RequestParam Long periodoId) {
        return ResponseEntity.ok(asignacionDocenteService.getOfertasPorProceso(procesoId, periodoId));
    }

    /**
     * Obtiene ofertas académicas filtradas por proceso educativo y periodo lectivo,
     * incluyendo información de las asignaturas correspondientes y su estado de asignación.
     * Este endpoint sigue las reglas específicas de asignaturas por proceso educativo:
     * - IPS (1° grado): Solo "Unidades Integradas" y "Proyectos Escolares"
     * - FCAP (2°-4° grado): "Unidades de Aprendizaje Integrado", "Inglés", "Proyectos Escolares"
     * - DDTE (5°-7° grado): "Unidades de Aprendizaje Integrado", "Inglés", "Proyectos Escolares"
     * - PAI (8°-10° grado): Todas las asignaturas configuradas
     */
    @GetMapping("/ofertas-por-proceso/{procesoId}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<OfertaProcesoDTO>> getOfertasFiltradas(
            @PathVariable Long procesoId,
            @RequestParam Long periodoId) {
        return ResponseEntity.ok(asignacionDocenteService.getOfertasPorProceso(procesoId, periodoId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_DOCENTE', 'ROLE_RECTOR')")
    public ResponseEntity<List<AsignacionDocenteListResponse>> getAllAsignaciones() {
        return ResponseEntity.ok(asignacionDocenteService.getAllAsignaciones());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DOCENTE', 'ROLE_RECTOR')")
    public ResponseEntity<AsignacionDocenteDTO> getAsignacionById(@PathVariable Long id) {
        return ResponseEntity.ok(asignacionDocenteService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, Object>> createAsignacion(
            @Valid @RequestBody AsignacionDocenteDTO asignacionDTO) {
        AsignacionDocenteDTO created = asignacionDocenteService.saveAsignacion(asignacionDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Asignación creada exitosamente");
        response.put("asignacion", created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, Object>> updateAsignacion(
            @PathVariable Long id,
            @Valid @RequestBody AsignacionDocenteDTO asignacionDTO) {
        // Obtener datos antes de actualizar para generar un mensaje específico
        AsignacionDocenteDTO asignacionAnterior = asignacionDocenteService.findById(id);
        
        // Realizar la actualización
        asignacionDocenteService.updateAsignacion(id, asignacionDTO);
        
        // Obtener la entidad actualizada
        AsignacionDocenteDTO updated = asignacionDocenteService.findById(id);
        
        // Generar mensaje descriptivo sobre los cambios realizados
        StringBuilder mensajeBuilder = new StringBuilder("Asignación actualizada: ");
        
        if (!asignacionAnterior.getDocenteId().equals(updated.getDocenteId())) {
            mensajeBuilder.append("Cambio de docente, ");
        }
        
        if (!asignacionAnterior.getOfertaId().equals(updated.getOfertaId())) {
            mensajeBuilder.append("Cambio de oferta, ");
        }
        
        if (!asignacionAnterior.getAsignaturaId().equals(updated.getAsignaturaId())) {
            mensajeBuilder.append("Cambio de asignatura, ");
        }
        
        if (!asignacionAnterior.getPeriodoId().equals(updated.getPeriodoId())) {
            mensajeBuilder.append("Cambio de período, ");
        }
        
        if (asignacionAnterior.getEstado() != updated.getEstado()) {
            mensajeBuilder.append("Cambio de estado, ");
        }
        
        String mensaje = mensajeBuilder.toString();
        if (mensaje.endsWith(", ")) {
            mensaje = mensaje.substring(0, mensaje.length() - 2);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("asignacion", updated);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, String>> deleteAsignacion(@PathVariable Long id) {
        // Obtener información de la asignación antes de eliminarla
        AsignacionDocenteDTO asignacion = asignacionDocenteService.findById(id);
        String infoAsignacion = String.format(
            "ID: %d, Docente: %s, Asignatura: %s, Oferta: %s",
            asignacion.getAsignacionId(),
            asignacion.getDocenteNombres(),
            asignacion.getAsignatura(),
            asignacion.getCurso() + " " + asignacion.getParalelo()
        );
        
        // Eliminar la asignación
        asignacionDocenteService.deleteAsignacion(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Asignación eliminada exitosamente");
        response.put("detalleEliminado", infoAsignacion);
        
        return ResponseEntity.ok(response);
    }
} 