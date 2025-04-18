package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.AsignacionDocenteDTO;
import com.seibe.docente.application.dto.AsignacionDocenteListResponse;
import com.seibe.docente.domain.entity.*;
import com.seibe.docente.domain.repository.AsignacionDocenteRepository;
import com.seibe.docente.domain.repository.AsignaturaRepository;
import com.seibe.docente.domain.repository.DocenteRepository;
import com.seibe.docente.domain.repository.OfertaRepository;
import com.seibe.docente.domain.repository.PeriodoRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import com.seibe.docente.infrastructure.mapper.AsignacionDocenteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsignacionDocenteServiceImplTest {

    @Mock
    private AsignacionDocenteRepository asignacionDocenteRepository;
    
    @Mock
    private OfertaRepository ofertaRepository;
    
    @Mock
    private DocenteRepository docenteRepository;
    
    @Mock
    private AsignaturaRepository asignaturaRepository;
    
    @Mock
    private PeriodoRepository periodoRepository;
    
    @Mock
    private AsignacionDocenteMapper mapper;
    
    @InjectMocks
    private AsignacionDocenteServiceImpl asignacionDocenteService;
    
    private AsignacionDocente asignacionDocente;
    private AsignacionDocenteDTO asignacionDTO;
    private List<AsignacionDocente> asignacionDocenteList;
    private List<AsignacionDocenteListResponse> responseList;
    private Docente docente;
    private Oferta oferta;
    private Periodo periodo;
    private Asignatura asignatura;
    
    @BeforeEach
    void setUp() {
        // Crear docente
        docente = new Docente();
        docente.setId(1L);
        docente.setDocNombre("Juan Pérez");
        docente.setDocIdentificacion("1234567890");

        // Crear oferta
        oferta = new Oferta();
        oferta.setId(1L);
        oferta.setOfeCurso("Primero");
        oferta.setOfeParalelo("A");

        // Crear periodo
        periodo = new Periodo();
        periodo.setId(1L);
        periodo.setNombre("2023-2024");

        // Crear asignatura
        asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setNombre("Matemáticas");
        
        // Crear asignación
        asignacionDocente = new AsignacionDocente();
        asignacionDocente.setId(1L);
        asignacionDocente.setDocente(docente);
        asignacionDocente.setOferta(oferta);
        asignacionDocente.setPeriodo(periodo);
        asignacionDocente.setAsignatura(asignatura);
        asignacionDocente.setEstado(true);
        
        // Crear DTO
        asignacionDTO = new AsignacionDocenteDTO();
        asignacionDTO.setAsignacionId(1L);
        asignacionDTO.setDocenteId(1L);
        asignacionDTO.setOfertaId(1L);
        asignacionDTO.setPeriodoId(1L);
        asignacionDTO.setAsignaturaId(1L);
        asignacionDTO.setEstado(true);
        
        // Crear listas
        asignacionDocenteList = new ArrayList<>();
        asignacionDocenteList.add(asignacionDocente);
        
        AsignacionDocenteListResponse response = new AsignacionDocenteListResponse();
        response.setAsignacionId(1L);
        response.setDocenteNombres("Juan Pérez");
        response.setDocenteCedula("1234567890");
        response.setCurso("Primero");
        response.setParalelo("A");
        response.setAsignatura("Matemáticas");
        response.setActivo(true);
        
        responseList = new ArrayList<>();
        responseList.add(response);
    }
    
    @Test
    void getAllAsignaciones_ShouldReturnList() {
        when(asignacionDocenteRepository.findAll()).thenReturn(asignacionDocenteList);
        when(mapper.toListResponse(asignacionDocenteList)).thenReturn(responseList);
        
        List<AsignacionDocenteListResponse> result = asignacionDocenteService.getAllAsignaciones();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAsignacionId());
        assertEquals("Juan Pérez", result.get(0).getDocenteNombres());
        assertEquals("1234567890", result.get(0).getDocenteCedula());
        verify(asignacionDocenteRepository).findAll();
        verify(mapper).toListResponse(asignacionDocenteList);
    }
    
    @Test
    void saveAsignacion_ShouldSaveEntity() {
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta));
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura));
        when(periodoRepository.findById(1L)).thenReturn(Optional.of(periodo));
        when(asignacionDocenteRepository.save(any(AsignacionDocente.class))).thenReturn(asignacionDocente);
        when(mapper.toDto(asignacionDocente)).thenReturn(asignacionDTO);
        
        AsignacionDocenteDTO result = asignacionDocenteService.saveAsignacion(asignacionDTO);
        
        assertNotNull(result);
        assertEquals(1L, result.getAsignacionId());
        verify(docenteRepository).findById(1L);
        verify(ofertaRepository).findById(1L);
        verify(asignaturaRepository).findById(1L);
        verify(periodoRepository).findById(1L);
        verify(asignacionDocenteRepository).save(any(AsignacionDocente.class));
    }
    
    @Test
    void updateAsignacion_WithValidId_ShouldUpdateEntity() {
        when(asignacionDocenteRepository.findById(1L)).thenReturn(Optional.of(asignacionDocente));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta));
        when(asignacionDocenteRepository.save(asignacionDocente)).thenReturn(asignacionDocente);
        
        asignacionDocenteService.updateAsignacion(1L, asignacionDTO);
        
        verify(asignacionDocenteRepository).findById(1L);
        verify(docenteRepository).findById(1L);
        verify(ofertaRepository).findById(1L);
        verify(mapper).updateEntity(asignacionDTO, asignacionDocente);
        verify(asignacionDocenteRepository).save(asignacionDocente);
    }
    
    @Test
    void updateAsignacion_WithInvalidId_ShouldThrowException() {
        when(asignacionDocenteRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            asignacionDocenteService.updateAsignacion(999L, asignacionDTO);
        });
        
        verify(asignacionDocenteRepository).findById(999L);
        verify(asignacionDocenteRepository, never()).save(any());
    }
    
    @Test
    void deleteAsignacion_WithValidId_ShouldDeleteEntity() {
        when(asignacionDocenteRepository.findById(1L)).thenReturn(Optional.of(asignacionDocente));
        
        asignacionDocenteService.deleteAsignacion(1L);
        
        verify(asignacionDocenteRepository).findById(1L);
        verify(asignacionDocenteRepository).delete(asignacionDocente);
    }
    
    @Test
    void deleteAsignacion_WithInvalidId_ShouldThrowException() {
        when(asignacionDocenteRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            asignacionDocenteService.deleteAsignacion(999L);
        });
        
        verify(asignacionDocenteRepository).findById(999L);
        verify(asignacionDocenteRepository, never()).delete(any());
    }
} 