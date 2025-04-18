package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.AsignacionDocenteDTO;
import com.seibe.docente.application.dto.AsignacionDocenteListResponse;
import com.seibe.docente.application.dto.OfertaProcesoDTO;

import java.util.List;

public interface AsignacionDocenteService {
    List<OfertaProcesoDTO> getOfertasPorProceso(Long procesoId, Long periodoId);
    
    List<AsignacionDocenteListResponse> getAllAsignaciones();
    
    AsignacionDocenteDTO findById(Long id);
    
    AsignacionDocenteDTO saveAsignacion(AsignacionDocenteDTO asignacion);
    
    AsignacionDocenteDTO updateAsignacion(Long id, AsignacionDocenteDTO asignacion);
    
    void deleteAsignacion(Long id);
} 