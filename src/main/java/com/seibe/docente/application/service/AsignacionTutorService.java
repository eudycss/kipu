package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.AsignacionTutorDTO;
import com.seibe.docente.application.dto.DocenteDTO;

import java.util.List;

public interface AsignacionTutorService {
    List<AsignacionTutorDTO> findAll();
    AsignacionTutorDTO findById(Long id);
    AsignacionTutorDTO findByOfertaId(Long ofertaId);
    AsignacionTutorDTO create(AsignacionTutorDTO dto);
    AsignacionTutorDTO update(Long id, AsignacionTutorDTO dto);
    void delete(Long id);
    List<AsignacionTutorDTO> findByDocenteId(Long docenteId);
    List<DocenteDTO> findDocentesActivos();
    
    // Nuevos métodos para validación
    DocenteDTO findDocenteById(Long docenteId);
    boolean existsByDocenteIdAndOfertaId(Long docenteId, Long ofertaId);
} 