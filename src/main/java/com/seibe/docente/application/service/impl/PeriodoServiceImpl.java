package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.PeriodoDTO;
import com.seibe.docente.application.service.PeriodoService;
import com.seibe.docente.domain.entity.Periodo;
import com.seibe.docente.domain.repository.PeriodoRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import com.seibe.docente.infrastructure.exception.UniqueConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PeriodoServiceImpl implements PeriodoService {

    private final PeriodoRepository periodoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PeriodoDTO> findAll() {
        return periodoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodoDTO findById(Long id) {
        return periodoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodoDTO findByNombre(String nombre) {
        return periodoRepository.findByNombre(nombre)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con nombre: " + nombre));
    }

    @Override
    public PeriodoDTO create(PeriodoDTO periodoDTO) {
        if (periodoRepository.existsByNombre(periodoDTO.getNombre())) {
            throw new UniqueConstraintViolationException("Ya existe un periodo con el nombre: " + periodoDTO.getNombre());
        }
        Periodo periodo = convertToEntity(periodoDTO);
        periodo.setActivo(false); // Por defecto, un nuevo periodo se crea inactivo
        return convertToDTO(periodoRepository.save(periodo));
    }

    @Override
    public PeriodoDTO update(Long id, PeriodoDTO periodoDTO) {
        Periodo existingPeriodo = periodoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con ID: " + id));

        if (!existingPeriodo.getNombre().equals(periodoDTO.getNombre()) &&
                periodoRepository.existsByNombre(periodoDTO.getNombre())) {
            throw new UniqueConstraintViolationException("Ya existe un periodo con el nombre: " + periodoDTO.getNombre());
        }

        existingPeriodo.setNombre(periodoDTO.getNombre());
        existingPeriodo.setFechaInicio(periodoDTO.getFechaInicio());
        existingPeriodo.setFechaFin(periodoDTO.getFechaFin());
        // No actualizamos el estado activo aquí, eso se maneja en activarPeriodo

        return convertToDTO(periodoRepository.save(existingPeriodo));
    }

    @Override
    public void delete(Long id) {
        if (!periodoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Periodo no encontrado con ID: " + id);
        }
        periodoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodoDTO findPeriodoActivo() {
        return periodoRepository.findByActivo(true)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No hay un periodo activo"));
    }

    @Override
    public PeriodoDTO activarPeriodo(Long id) {
        Periodo periodoToActivate = periodoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado con ID: " + id));

        // Desactivar el periodo activo actual si existe
        periodoRepository.findByActivo(true)
                .ifPresent(activePeriodo -> {
                    activePeriodo.setActivo(false);
                    periodoRepository.save(activePeriodo);
                });

        // Activar el nuevo periodo
        periodoToActivate.setActivo(true);
        return convertToDTO(periodoRepository.save(periodoToActivate));
    }

    private PeriodoDTO convertToDTO(Periodo periodo) {
        PeriodoDTO dto = new PeriodoDTO();
        dto.setId(periodo.getId());
        dto.setNombre(periodo.getNombre());
        dto.setFechaInicio(periodo.getFechaInicio());
        dto.setFechaFin(periodo.getFechaFin());
        dto.setActivo(periodo.getActivo());
        return dto;
    }

    private Periodo convertToEntity(PeriodoDTO dto) {
        Periodo periodo = new Periodo();
        periodo.setId(dto.getId());
        periodo.setNombre(dto.getNombre());
        periodo.setFechaInicio(dto.getFechaInicio());
        periodo.setFechaFin(dto.getFechaFin());
        periodo.setActivo(dto.isActivo());
        return periodo;
    }
} 