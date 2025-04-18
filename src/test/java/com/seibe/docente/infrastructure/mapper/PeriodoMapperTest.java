package com.seibe.docente.infrastructure.mapper;

import com.seibe.docente.application.dto.PeriodoDTO;
import com.seibe.docente.domain.entity.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeriodoMapperTest {

    private PeriodoMapper mapper;
    private Periodo periodo;
    private PeriodoDTO periodoDTO;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PeriodoMapper.class);
        
        // Setup test entity
        periodo = new Periodo();
        periodo.setId(1L);
        periodo.setNombre("Periodo 2024-1");
        periodo.setFechaInicio(LocalDate.of(2024, 1, 1));
        periodo.setFechaFin(LocalDate.of(2024, 6, 30));
        periodo.setActivo(true);
        periodo.setEstado(true);

        // Setup test DTO
        periodoDTO = new PeriodoDTO();
        periodoDTO.setId(1L);
        periodoDTO.setNombre("Periodo 2024-1");
        periodoDTO.setFechaInicio(LocalDate.of(2024, 1, 1));
        periodoDTO.setFechaFin(LocalDate.of(2024, 6, 30));
        periodoDTO.setActivo(true);
    }

    @Test
    void toDTO_ShouldMapAllFields() {
        // When
        PeriodoDTO result = mapper.toDTO(periodo);

        // Then
        assertNotNull(result);
        assertEquals(periodo.getId(), result.getId());
        assertEquals(periodo.getNombre(), result.getNombre());
        assertEquals(periodo.getFechaInicio(), result.getFechaInicio());
        assertEquals(periodo.getFechaFin(), result.getFechaFin());
        assertEquals(periodo.getActivo(), result.isActivo());
    }

    @Test
    void toDTO_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        // When
        Periodo result = mapper.toEntity(periodoDTO);

        // Then
        assertNotNull(result);
        assertEquals(periodoDTO.getId(), result.getId());
        assertEquals(periodoDTO.getNombre(), result.getNombre());
        assertEquals(periodoDTO.getFechaInicio(), result.getFechaInicio());
        assertEquals(periodoDTO.getFechaFin(), result.getFechaFin());
        assertEquals(periodoDTO.isActivo(), result.getActivo());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDTOList_ShouldMapAllPeriodos() {
        // Given
        List<Periodo> periodos = Arrays.asList(periodo);

        // When
        List<PeriodoDTO> result = mapper.toDTOList(periodos);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        PeriodoDTO firstResult = result.get(0);
        assertEquals(periodo.getId(), firstResult.getId());
        assertEquals(periodo.getNombre(), firstResult.getNombre());
        assertEquals(periodo.getFechaInicio(), firstResult.getFechaInicio());
        assertEquals(periodo.getFechaFin(), firstResult.getFechaFin());
        assertEquals(periodo.getActivo(), firstResult.isActivo());
    }

    @Test
    void toDTOList_ShouldReturnEmptyList_WhenInputIsNull() {
        assertTrue(mapper.toDTOList(null).isEmpty());
    }
} 