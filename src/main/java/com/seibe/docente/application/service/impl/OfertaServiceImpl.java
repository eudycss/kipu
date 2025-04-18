package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.AsignaturaResponseDTO;
import com.seibe.docente.application.dto.DocenteDisponibilidadDTO;
import com.seibe.docente.application.dto.OfertaDTO;
import com.seibe.docente.application.service.OfertaService;
import com.seibe.docente.domain.entity.Area;
import com.seibe.docente.domain.entity.Docente;
import com.seibe.docente.domain.entity.Oferta;
import com.seibe.docente.domain.entity.Periodo;
import com.seibe.docente.domain.entity.ProcesoEducativo;
import com.seibe.docente.domain.entity.Asignatura;
import com.seibe.docente.domain.repository.AreaRepository;
import com.seibe.docente.domain.repository.AsignacionDocenteRepository;
import com.seibe.docente.domain.repository.AsignacionTutorRepository;
import com.seibe.docente.domain.repository.AsignaturaRepository;
import com.seibe.docente.domain.repository.DocenteRepository;
import com.seibe.docente.domain.repository.OfertaRepository;
import com.seibe.docente.domain.repository.PeriodoRepository;
import com.seibe.docente.domain.repository.ProcesoEducativoRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfertaServiceImpl implements OfertaService {
    private final OfertaRepository ofertaRepository;
    private final ProcesoEducativoRepository procesoRepository;
    private final PeriodoRepository periodoRepository;
    private final DocenteRepository docenteRepository;
    private final AsignacionDocenteRepository asignacionDocenteRepository;
    private final AsignacionTutorRepository asignacionTutorRepository;
    private final AreaRepository areaRepository;
    private final AsignaturaRepository asignaturaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OfertaDTO> findByProcesoAndPeriodo(Long procesoId, Long periodoId) {
        // Validar que el proceso educativo existe
        ProcesoEducativo proceso = procesoRepository.findById(procesoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proceso educativo no encontrado"));
        
        // Validar que el periodo existe
        Periodo periodo = periodoRepository.findById(periodoId)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo lectivo no encontrado"));
        
        // Obtener las ofertas relacionadas con el proceso y periodo
        List<Oferta> ofertas = ofertaRepository.findByGradoProcesoIdAndPeriodoId(procesoId, periodoId);
        
        // Convertir a DTOs
        return ofertas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public OfertaDTO findById(Long id) {
        return ofertaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocenteDisponibilidadDTO> findDocentesByOfertaId(Long ofertaId) {
        // Verificar que la oferta existe
        Oferta oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        
        // Obtener todos los docentes activos
        List<Docente> docentes = docenteRepository.findByDocEstadoTrue();
        
        // Obtener los IDs de docentes ya asignados como docentes a esta oferta
        List<Long> docentesAsignadosIds = asignacionDocenteRepository.findByOfertaId(ofertaId)
                .stream()
                .map(asignacion -> asignacion.getDocente().getId())
                .collect(Collectors.toList());
                
        // Obtener los IDs de docentes ya asignados como tutores a cualquier oferta
        // Un docente solo puede ser tutor de una oferta a la vez
        Set<Long> docentesTutoresIds = new HashSet<>();
        
        // Verificar si la oferta ya tiene un tutor asignado
        boolean ofertaTieneTutor = asignacionTutorRepository.existsByOfertaIdAndEstadoTrue(ofertaId);
        if (ofertaTieneTutor) {
            docentesTutoresIds.addAll(
                asignacionTutorRepository.findByOfertaIdAndEstadoTrue(ofertaId)
                    .map(asignacion -> asignacion.getDocente().getId())
                    .stream()
                    .collect(Collectors.toSet())
            );
        } else {
            // Si la oferta no tiene tutor, verificar todos los docentes que ya son tutores de otras ofertas
            docentes.forEach(docente -> {
                if (asignacionTutorRepository.existsByDocenteIdAndEstadoTrue(docente.getId())) {
                    docentesTutoresIds.add(docente.getId());
                }
            });
        }
        
        // Preparar listas para docentes disponibles e indisponibles
        List<DocenteDisponibilidadDTO> docentesDisponibles = new ArrayList<>();
        List<DocenteDisponibilidadDTO> docentesNoDisponibles = new ArrayList<>();
        
        // Clasificar a los docentes según su disponibilidad
        for (Docente docente : docentes) {
            DocenteDisponibilidadDTO dto = new DocenteDisponibilidadDTO();
            dto.setDocenteId(docente.getId());
            dto.setDocIdentificacion(docente.getDocIdentificacion());
            dto.setDocNombre(docente.getDocNombre());
            dto.setDocCorreo(docente.getDocCorreo());
            dto.setAreaId(docente.getAreaId());
            
            // Obtener nombre del área si existe
            if (docente.getAreaId() != null) {
                Optional<Area> area = areaRepository.findById(docente.getAreaId());
                dto.setAreaNombre(area.isPresent() ? area.get().getNombre() : null);
            }
            
            // Un docente NO está disponible si:
            // 1. Ya está asignado como docente a esta oferta, O
            // 2. Ya está asignado como tutor (ya sea a esta oferta u otra)
            boolean disponible = !docentesAsignadosIds.contains(docente.getId()) && 
                                !docentesTutoresIds.contains(docente.getId());
            
            dto.setDisponible(disponible);
            
            // Agregar a la lista correspondiente
            if (disponible) {
                docentesDisponibles.add(dto);
            } else {
                docentesNoDisponibles.add(dto);
            }
        }
        
        // Combinar las listas, primero los disponibles y luego los no disponibles
        List<DocenteDisponibilidadDTO> resultado = new ArrayList<>();
        resultado.addAll(docentesDisponibles);
        resultado.addAll(docentesNoDisponibles);
        
        return resultado;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AsignaturaResponseDTO> findAsignaturasByOfertaId(Long ofertaId) {
        // Verificar que la oferta existe
        Oferta oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        
        // Determinar el grado de la oferta para aplicar las reglas de asignaturas
        Long gradoId = oferta.getGrado().getId();
        
        // Lista para almacenar asignaturas aplicables
        List<Asignatura> asignaturasAplicables = new ArrayList<>();
        
        // Obtener todas las asignaturas activas
        List<Asignatura> todasAsignaturas = asignaturaRepository.findAll().stream()
                .filter(a -> "ACTIVO".equals(a.getEstado()))
                .collect(Collectors.toList());
        
        // Aplicar reglas según el ID del grado (basado en los datos mostrados en las imágenes)
        if (gradoId == 1) {
            // Para 1° grado (gradoId = 1): Solo "Unidades Integradas" y "Proyectos Escolares"
            asignaturasAplicables = todasAsignaturas.stream()
                .filter(a -> a.getNombre().equals("Unidades Integradas") || 
                             a.getNombre().equals("Proyectos Escolares"))
                .collect(Collectors.toList());
        } else if (gradoId >= 2 && gradoId <= 4) {
            // Para 2°-4° grado: Unidades de Aprendizaje Integrado, Inglés, Proyectos Escolares
            asignaturasAplicables = todasAsignaturas.stream()
                .filter(a -> a.getNombre().equals("Unidades Integradas") || // Asumiendo que se usa el mismo nombre
                             a.getNombre().equals("Inglés") || 
                             a.getNemonico().equals("ING") ||
                             a.getNombre().equals("Proyectos Escolares"))
                .collect(Collectors.toList());
        } else if (gradoId >= 5 && gradoId <= 7) {
            // Para 5°-7° grado: Igual que 2°-4°
            asignaturasAplicables = todasAsignaturas.stream()
                .filter(a -> a.getNombre().equals("Unidades Integradas") || // Asumiendo que se usa el mismo nombre
                             a.getNombre().equals("Inglés") || 
                             a.getNemonico().equals("ING") ||
                             a.getNombre().equals("Proyectos Escolares"))
                .collect(Collectors.toList());
        } else if (gradoId >= 8 && gradoId <= 10) {
            // Para 8°-10° grado: Todas las asignaturas del curriculum PAI
            // Como no vemos todas las asignaturas en los datos proporcionados, incluimos todas las activas
            asignaturasAplicables = todasAsignaturas;
        } else {
            // Para otros grados, por defecto mostrar todas las asignaturas activas
            asignaturasAplicables = todasAsignaturas;
        }
        
        // Obtener las asignaturas ya asignadas a esta oferta
        Set<Long> asignaturasAsignadasIds = asignacionDocenteRepository.findByOfertaId(ofertaId)
                .stream()
                .map(asignacion -> asignacion.getAsignatura().getId())
                .collect(Collectors.toSet());
        
        // Convertir a DTOs con información de asignación
        return asignaturasAplicables.stream()
                .map(asignatura -> {
                    AsignaturaResponseDTO dto = AsignaturaResponseDTO.builder()
                            .asignaturaId(asignatura.getId())
                            .nombre(asignatura.getNombre())
                            .nemonico(asignatura.getNemonico())
                            .areaId(asignatura.getArea().getId())
                            .areaNombre(asignatura.getArea().getNombre())
                            .asignado(asignaturasAsignadasIds.contains(asignatura.getId()))
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Extrae el número del grado a partir de su nemónico o descripción.
     * Por ejemplo, "1EGB" o "Primero EGB" debería devolver 1.
     */
    private int extraerNumeroGrado(String gradoTexto) {
        if (gradoTexto == null || gradoTexto.isEmpty()) {
            return 0;
        }
        
        // Intentar extraer números del texto
        try {
            // Primero intentamos encontrar un número al inicio del string
            if (gradoTexto.matches("^\\d.*")) {
                return Integer.parseInt(gradoTexto.replaceAll("\\D.*", ""));
            }
            
            // Alternativamente, buscamos por texto de números
            if (gradoTexto.toLowerCase().contains("primero") || gradoTexto.toLowerCase().contains("primer")) {
                return 1;
            } else if (gradoTexto.toLowerCase().contains("segundo")) {
                return 2;
            } else if (gradoTexto.toLowerCase().contains("tercero") || gradoTexto.toLowerCase().contains("tercer")) {
                return 3;
            } else if (gradoTexto.toLowerCase().contains("cuarto")) {
                return 4;
            } else if (gradoTexto.toLowerCase().contains("quinto")) {
                return 5;
            } else if (gradoTexto.toLowerCase().contains("sexto")) {
                return 6;
            } else if (gradoTexto.toLowerCase().contains("séptimo") || gradoTexto.toLowerCase().contains("septimo")) {
                return 7;
            } else if (gradoTexto.toLowerCase().contains("octavo")) {
                return 8;
            } else if (gradoTexto.toLowerCase().contains("noveno")) {
                return 9;
            } else if (gradoTexto.toLowerCase().contains("décimo") || gradoTexto.toLowerCase().contains("decimo")) {
                return 10;
            }
            
            // Si no podemos determinar, devolvemos 0
            return 0;
        } catch (NumberFormatException e) {
            // En caso de error, devolvemos 0
            return 0;
        }
    }
    
    /**
     * Convierte una entidad Oferta a su correspondiente DTO
     */
    private OfertaDTO convertToDTO(Oferta oferta) {
        OfertaDTO dto = new OfertaDTO();
        dto.setOfertaId(oferta.getId());
        dto.setOfeCurso(oferta.getOfeCurso());
        dto.setOfeParalelo(oferta.getOfeParalelo());
        dto.setOfeAforo(oferta.getOfeAforo());
        dto.setOfeEstado(oferta.getOfeEstado());
        
        // Convertir grado si existe
        if (oferta.getGrado() != null) {
            OfertaDTO.GradoSimpleDTO gradoDTO = new OfertaDTO.GradoSimpleDTO();
            gradoDTO.setGradoId(oferta.getGrado().getId());
            gradoDTO.setDescripcion(oferta.getGrado().getDescripcion());
            gradoDTO.setNemonico(oferta.getGrado().getNemonico());
            dto.setGrado(gradoDTO);
        }
        
        return dto;
    }
} 