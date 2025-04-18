package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.AsignacionTutorDTO;
import com.seibe.docente.application.dto.DocenteDTO;
import com.seibe.docente.application.service.AsignacionTutorService;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asignaciones-tutor")
@RequiredArgsConstructor
public class AsignacionTutorController {

    private final AsignacionTutorService asignacionTutorService;

    /**
     * Obtiene todas las asignaciones de tutores
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<AsignacionTutorDTO>> findAll() {
        return ResponseEntity.ok(asignacionTutorService.findAll());
    }

    /**
     * Obtiene una asignación de tutor por su ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<AsignacionTutorDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(asignacionTutorService.findById(id));
    }

    /**
     * Obtiene el tutor asignado a una oferta específica
     */
    @GetMapping("/oferta/{ofertaId}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<?> findByOfertaId(@PathVariable Long ofertaId) {
        try {
            AsignacionTutorDTO asignacionTutor = asignacionTutorService.findByOfertaId(ofertaId);
            return ResponseEntity.ok(asignacionTutor);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", ex.getMessage()));
        }
    }

    /**
     * Crea una nueva asignación de tutor
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AsignacionTutorDTO asignacionTutorDTO) {
        try {
            // Validar datos de entrada
            if (asignacionTutorDTO.getDocenteId() == null) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "El ID del docente es obligatorio"));
            }
            
            if (asignacionTutorDTO.getOfertaId() == null) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "El ID de la oferta es obligatorio"));
            }
            
            // Verificar si el docente está activo
            try {
                DocenteDTO docente = asignacionTutorService.findDocenteById(asignacionTutorDTO.getDocenteId());
                if (!docente.getEstado()) {
                    return ResponseEntity.badRequest().body(Map.of(
                        "mensaje", "El docente no está activo. Debe activarlo antes de asignarlo como tutor.",
                        "docente", docente
                    ));
                }
                
                // Revisar si un docente ya tiene una asignación de materia en esta oferta
                boolean tieneMateriaEnCurso = asignacionTutorService.existsByDocenteIdAndOfertaId(
                    asignacionTutorDTO.getDocenteId(), 
                    asignacionTutorDTO.getOfertaId()
                );
                
                // Realizar la asignación
                AsignacionTutorDTO created = asignacionTutorService.create(asignacionTutorDTO);
                
                // Construir respuesta informativa
                Map<String, Object> response = new HashMap<>();
                String mensaje = String.format(
                    "Docente %s (ID: %d) asignado como tutor a la oferta ID %d exitosamente", 
                    docente.getDocNombre(), 
                    docente.getDocenteId(), 
                    asignacionTutorDTO.getOfertaId()
                );
                
                if (tieneMateriaEnCurso) {
                    mensaje += ". El docente ya tiene asignaturas en este curso.";
                }
                
                response.put("mensaje", mensaje);
                response.put("asignacion", created);
                response.put("docente", docente);
                response.put("tieneMaterias", tieneMateriaEnCurso);
                
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
                
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", e.getMessage()));
            }
        } catch (IllegalStateException e) {
            String errorMessage = e.getMessage();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", errorMessage);
            
            // Proporcionar información adicional según el tipo de error
            if (errorMessage.contains("ya es tutor de otra oferta")) {
                // Intentar obtener información sobre la oferta donde el docente ya es tutor
                try {
                    Long docenteId = asignacionTutorDTO.getDocenteId();
                    DocenteDTO docente = asignacionTutorService.findDocenteById(docenteId);
                    List<AsignacionTutorDTO> asignacionesExistentes = asignacionTutorService.findByDocenteId(docenteId);
                    
                    if (!asignacionesExistentes.isEmpty()) {
                        AsignacionTutorDTO asignacionExistente = asignacionesExistentes.get(0);
                        errorResponse.put("docente", docente);
                        errorResponse.put("asignacionExistente", asignacionExistente);
                        errorResponse.put("sugerencia", "El docente debe ser removido como tutor de la oferta actual antes de asignarlo a una nueva.");
                    }
                } catch (Exception ex) {
                    // Ignorar errores al buscar información adicional
                }
            }
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Asigna un tutor a una oferta específica
     */
    @PostMapping("/oferta/{ofertaId}/docente/{docenteId}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<?> asignarTutor(@PathVariable Long ofertaId, @PathVariable Long docenteId) {
        try {
            // Verificar si el docente está activo
            DocenteDTO docente = asignacionTutorService.findDocenteById(docenteId);
            if (!docente.getEstado()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", 
                    "El docente no está activo. Debe activarlo antes de asignarlo como tutor."));
            }
            
            // Revisar si un docente ya tiene una asignación en una oferta específica
            boolean tieneMateriaEnCurso = asignacionTutorService.existsByDocenteIdAndOfertaId(docenteId, ofertaId);
            
            // Crear un DTO con los datos mínimos necesarios
            AsignacionTutorDTO dto = new AsignacionTutorDTO();
            dto.setOfertaId(ofertaId);
            dto.setDocenteId(docenteId);
            
            AsignacionTutorDTO result = asignacionTutorService.create(dto);
            
            Map<String, Object> response = new HashMap<>();
            String mensaje = String.format(
                "Docente %s (ID: %d) asignado como tutor a la oferta ID %d exitosamente", 
                docente.getNombre(), docenteId, ofertaId
            );
            
            if (tieneMateriaEnCurso) {
                mensaje += ". El docente ya tiene asignaturas en este curso.";
            }
            
            response.put("mensaje", mensaje);
            response.put("asignacion", result);
            response.put("tieneMaterias", tieneMateriaEnCurso);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", e.getMessage()));
        }
    }

    /**
     * Actualiza una asignación de tutor existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id, 
            @RequestBody AsignacionTutorDTO asignacionTutorDTO) {
        
        // Obtener datos antes de actualizar para generar un mensaje específico
        AsignacionTutorDTO asignacionAnterior = asignacionTutorService.findById(id);
        
        // Realizar la actualización
        AsignacionTutorDTO updated = asignacionTutorService.update(id, asignacionTutorDTO);
        
        // Generar mensaje descriptivo sobre los cambios realizados
        StringBuilder mensajeBuilder = new StringBuilder("Asignación de tutor actualizada: ");
        
        if (!asignacionAnterior.getDocenteId().equals(updated.getDocenteId())) {
            mensajeBuilder.append("Cambio de tutor, ");
        }
        
        if (!asignacionAnterior.getOfertaId().equals(updated.getOfertaId())) {
            mensajeBuilder.append("Cambio de oferta, ");
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

    /**
     * Cambia el tutor asignado a una oferta
     */
    @PutMapping("/oferta/{ofertaId}/docente/{nuevoDocenteId}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<?> cambiarTutor(
            @PathVariable Long ofertaId, 
            @PathVariable Long nuevoDocenteId) {
        try {
            // Buscar la asignación actual
            AsignacionTutorDTO asignacionActual = asignacionTutorService.findByOfertaId(ofertaId);
            Long docenteAnteriorId = asignacionActual.getDocenteId();
            String nombreDocenteAnterior = asignacionActual.getDocenteNombre();
            
            // Desactivar la asignación actual y crear una nueva
            asignacionActual.setEstado(false);
            asignacionTutorService.update(asignacionActual.getAsignacionId(), asignacionActual);
            
            // Crear una nueva asignación
            AsignacionTutorDTO nuevaAsignacion = new AsignacionTutorDTO();
            nuevaAsignacion.setOfertaId(ofertaId);
            nuevaAsignacion.setDocenteId(nuevoDocenteId);
            
            AsignacionTutorDTO result = asignacionTutorService.create(nuevaAsignacion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", String.format(
                "Tutor cambiado exitosamente de %s (ID: %d) a %s (ID: %d) para la oferta ID %d",
                nombreDocenteAnterior, docenteAnteriorId,
                result.getDocenteNombre(), nuevoDocenteId,
                ofertaId
            ));
            response.put("asignacionAnterior", asignacionActual);
            response.put("asignacionNueva", result);
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    /**
     * Elimina una asignación de tutor
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        // Obtener información de la asignación antes de eliminarla
        AsignacionTutorDTO asignacion = asignacionTutorService.findById(id);
        String infoAsignacion = String.format(
            "ID: %d, Tutor: %s, Oferta: %s %s, Estado: %s",
            asignacion.getAsignacionId(),
            asignacion.getDocenteNombre(),
            asignacion.getOfertaCurso(),
            asignacion.getOfertaParalelo(),
            asignacion.getEstado() ? "Activo" : "Inactivo"
        );
        
        // Eliminar la asignación
        asignacionTutorService.delete(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Asignación de tutor eliminada exitosamente");
        response.put("detalleEliminado", infoAsignacion);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las asignaciones de tutor para un docente específico
     */
    @GetMapping("/docente/{docenteId}")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<AsignacionTutorDTO>> findByDocenteId(@PathVariable Long docenteId) {
        return ResponseEntity.ok(asignacionTutorService.findByDocenteId(docenteId));
    }

     /**
     * Obtiene todas los docentes activos para ser asignados como tutor
     */
    @GetMapping("/docentes-activos")
    @PreAuthorize("hasRole('ROLE_RECTOR')")
    public ResponseEntity<List<DocenteDTO>> findDocentesActivos() {
        return ResponseEntity.ok(asignacionTutorService.findDocentesActivos());
    }
} 