package com.seibe.docente.infrastructure.mapper;

import com.seibe.docente.application.dto.PeriodoDTO;
import com.seibe.docente.domain.entity.Periodo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PeriodoMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "activo", source = "activo")
    PeriodoDTO toDTO(Periodo periodo);

    List<PeriodoDTO> toDTOList(List<Periodo> periodos);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "activo", source = "activo")
    @Mapping(target = "estado", constant = "true")
    Periodo toEntity(PeriodoDTO dto);
} 