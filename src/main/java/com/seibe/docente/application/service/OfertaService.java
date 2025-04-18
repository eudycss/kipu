package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.AsignaturaResponseDTO;
import com.seibe.docente.application.dto.DocenteDisponibilidadDTO;
import com.seibe.docente.application.dto.OfertaDTO;
import java.util.List;

public interface OfertaService {
    /**
     * Obtiene todas las ofertas relacionadas a un proceso educativo específico
     * para un periodo lectivo determinado.
     * 
     * @param procesoId ID del proceso educativo
     * @param periodoId ID del periodo lectivo
     * @return Lista de ofertas relacionadas
     */
    List<OfertaDTO> findByProcesoAndPeriodo(Long procesoId, Long periodoId);
    
    /**
     * Obtiene una oferta por su ID
     * 
     * @param id ID de la oferta
     * @return La oferta encontrada
     */
    OfertaDTO findById(Long id);
    
    /**
     * Obtiene la lista de docentes disponibles y no disponibles para una oferta.
     * Los docentes se ordenan mostrando primero los disponibles y luego los no disponibles.
     * 
     * @param ofertaId ID de la oferta
     * @return Lista de docentes con información de disponibilidad
     */
    List<DocenteDisponibilidadDTO> findDocentesByOfertaId(Long ofertaId);
    
    /**
     * Obtiene la lista de asignaturas asociadas a una oferta específica.
     * Para cada asignatura, se indica si ya está asignada a un docente.
     * 
     * @param ofertaId ID de la oferta
     * @return Lista de asignaturas con su información de asignación
     */
    List<AsignaturaResponseDTO> findAsignaturasByOfertaId(Long ofertaId);
} 