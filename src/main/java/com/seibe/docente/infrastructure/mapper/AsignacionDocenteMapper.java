package com.seibe.docente.infrastructure.mapper;

import com.seibe.docente.application.dto.AsignacionDocenteDTO;
import com.seibe.docente.application.dto.AsignacionDocenteListResponse;
import com.seibe.docente.application.dto.OfertaAsignacionDTO;
import com.seibe.docente.domain.entity.*;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utiliza la implementación AsignacionDocenteMapperOriginal
 * Esta interfaz se mantiene para compatibilidad con código existente
 */
@Component
public class AsignacionDocenteMapper {
    
    @Autowired
    private AsignacionDocenteMapperOriginal original;
    
    public AsignacionDocenteDTO toDto(AsignacionDocente asignacion) {
        return original.toDto(asignacion);
    }
    
    public List<AsignacionDocenteDTO> toDtoList(List<AsignacionDocente> asignaciones) {
        return original.toDtoList(asignaciones);
    }
    
    public AsignacionDocente toEntity(AsignacionDocenteDTO dto) {
        return original.toEntity(dto);
    }
    
    public AsignacionDocenteListResponse toListResponse(AsignacionDocente asignacion) {
        return original.toListResponse(asignacion);
    }
    
    public List<AsignacionDocenteListResponse> toListResponse(List<AsignacionDocente> asignaciones) {
        return original.toListResponse(asignaciones);
    }
    
    public void updateEntity(AsignacionDocenteDTO dto, AsignacionDocente entity) {
        original.updateEntity(dto, entity);
    }
    
    public OfertaAsignacionDTO toOfertaAsignacionDTO(Oferta oferta) {
        return original.toOfertaAsignacionDTO(oferta);
    }
} 