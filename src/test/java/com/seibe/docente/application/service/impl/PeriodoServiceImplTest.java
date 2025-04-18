package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.PeriodoDTO;
import com.seibe.docente.domain.entity.Periodo;
import com.seibe.docente.domain.repository.PeriodoRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import com.seibe.docente.infrastructure.exception.UniqueConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeriodoServiceImplTest {

    @Mock
    private PeriodoRepository periodoRepository;

    @InjectMocks
    private PeriodoServiceImpl periodoService;

    private Periodo periodo;
    private PeriodoDTO periodoDTO;

    @BeforeEach
    void setUp() {
        periodo = new Periodo();
        periodo.setId(1L);
        periodo.setNombre("2024-1");
        periodo.setFechaInicio(LocalDate.of(2024, 1, 1));
        periodo.setFechaFin(LocalDate.of(2024, 6, 30));
        periodo.setActivo(true);
        periodo.setEstado(true);

        periodoDTO = new PeriodoDTO();
        periodoDTO.setId(1L);
        periodoDTO.setNombre("2024-1");
        periodoDTO.setFechaInicio(LocalDate.of(2024, 1, 1));
        periodoDTO.setFechaFin(LocalDate.of(2024, 6, 30));
        periodoDTO.setActivo(true);
    }

    @Test
    void findAll_ShouldReturnListOfPeriodos() {
        when(periodoRepository.findAll()).thenReturn(Arrays.asList(periodo));

        List<PeriodoDTO> result = periodoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(periodoDTO.getNombre(), result.get(0).getNombre());
        assertEquals(periodoDTO.getFechaInicio(), result.get(0).getFechaInicio());
        assertEquals(periodoDTO.getFechaFin(), result.get(0).getFechaFin());
        assertEquals(periodoDTO.isActivo(), result.get(0).isActivo());
    }

    @Test
    void findById_WhenExists_ShouldReturnPeriodo() {
        when(periodoRepository.findById(1L)).thenReturn(Optional.of(periodo));

        PeriodoDTO result = periodoService.findById(1L);

        assertNotNull(result);
        assertEquals(periodoDTO.getNombre(), result.getNombre());
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(periodoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> periodoService.findById(1L));
    }

    @Test
    void create_WhenNameNotExists_ShouldCreatePeriodo() {
        when(periodoRepository.existsByNombre(any())).thenReturn(false);
        when(periodoRepository.save(any(Periodo.class))).thenReturn(periodo);

        PeriodoDTO result = periodoService.create(periodoDTO);

        assertNotNull(result);
        assertEquals(periodoDTO.getNombre(), result.getNombre());
        verify(periodoRepository).save(any(Periodo.class));
    }

    @Test
    void create_WhenNameExists_ShouldThrowException() {
        when(periodoRepository.existsByNombre(any())).thenReturn(true);

        assertThrows(UniqueConstraintViolationException.class, () -> periodoService.create(periodoDTO));
        verify(periodoRepository, never()).save(any(Periodo.class));
    }

    @Test
    void update_WhenExists_ShouldUpdatePeriodo() {
        when(periodoRepository.findById(1L)).thenReturn(Optional.of(periodo));
        when(periodoRepository.save(any(Periodo.class))).thenReturn(periodo);

        PeriodoDTO result = periodoService.update(1L, periodoDTO);

        assertNotNull(result);
        assertEquals(periodoDTO.getNombre(), result.getNombre());
        verify(periodoRepository).save(any(Periodo.class));
    }

    @Test
    void update_WhenNotExists_ShouldThrowException() {
        when(periodoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> periodoService.update(1L, periodoDTO));
        verify(periodoRepository, never()).save(any(Periodo.class));
    }

    @Test
    void delete_WhenExists_ShouldDeletePeriodo() {
        when(periodoRepository.existsById(1L)).thenReturn(true);

        periodoService.delete(1L);

        verify(periodoRepository).deleteById(1L);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowException() {
        when(periodoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> periodoService.delete(1L));
        verify(periodoRepository, never()).deleteById(any());
    }

    @Test
    void findPeriodoActivo_WhenExists_ShouldReturnActivePeriodo() {
        when(periodoRepository.findByActivoTrue()).thenReturn(Optional.of(periodo));

        PeriodoDTO result = periodoService.findPeriodoActivo();

        assertNotNull(result);
        assertTrue(result.isActivo());
        assertEquals(periodoDTO.getNombre(), result.getNombre());
    }

    @Test
    void findPeriodoActivo_WhenNotExists_ShouldThrowException() {
        when(periodoRepository.findByActivoTrue()).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> periodoService.findPeriodoActivo());
    }
} 