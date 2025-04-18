package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.AsignaturaDTO;
import com.seibe.docente.application.service.AsignaturaService;
import com.seibe.docente.domain.entity.Area;
import com.seibe.docente.domain.entity.Asignatura;
import com.seibe.docente.domain.repository.AreaRepository;
import com.seibe.docente.domain.repository.AsignaturaRepository;
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
public class AsignaturaServiceImpl implements AsignaturaService {
    private final AsignaturaRepository asignaturaRepository;
    private final AreaRepository areaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AsignaturaDTO> findAll() {
        return asignaturaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AsignaturaDTO findById(Long id) {
        return asignaturaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignaturaDTO> findByAreaId(Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));
        return asignaturaRepository.findByArea(area).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AsignaturaDTO findByNombreAndAreaId(String nombre, Long areaId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));
        return asignaturaRepository.findByNombreAndArea(nombre, area)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));
    }

    @Override
    public AsignaturaDTO create(AsignaturaDTO asignaturaDTO) {
        Area area = areaRepository.findById(asignaturaDTO.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));

        if (asignaturaRepository.existsByNombre(asignaturaDTO.getNombre())) {
            throw new UniqueConstraintViolationException("Ya existe una asignatura con este nombre");
        }

        if (asignaturaRepository.existsByNombreAndArea(asignaturaDTO.getNombre(), area)) {
            throw new UniqueConstraintViolationException("Ya existe una asignatura con este nombre en el área especificada");
        }

        Asignatura asignatura = convertToEntity(asignaturaDTO);
        return convertToDTO(asignaturaRepository.save(asignatura));
    }

    @Override
    public AsignaturaDTO update(Long id, AsignaturaDTO asignaturaDTO) {
        Asignatura existingAsignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        Area area = areaRepository.findById(asignaturaDTO.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));

        if (!existingAsignatura.getNombre().equals(asignaturaDTO.getNombre())) {
            if (asignaturaRepository.existsByNombre(asignaturaDTO.getNombre())) {
                throw new UniqueConstraintViolationException("Ya existe una asignatura con este nombre");
            }
        }

        if (!existingAsignatura.getNombre().equals(asignaturaDTO.getNombre()) ||
            !existingAsignatura.getArea().getId().equals(asignaturaDTO.getAreaId())) {
            if (asignaturaRepository.existsByNombreAndArea(asignaturaDTO.getNombre(), area)) {
                throw new UniqueConstraintViolationException("Ya existe una asignatura con este nombre en el área especificada");
            }
        }

        Asignatura asignatura = convertToEntity(asignaturaDTO);
        asignatura.setId(id);
        return convertToDTO(asignaturaRepository.save(asignatura));
    }

    @Override
    public void delete(Long id) {
        if (!asignaturaRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Asignatura no encontrada");
        }
        asignaturaRepository.deleteById(id);
    }

    private AsignaturaDTO convertToDTO(Asignatura asignatura) {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(asignatura.getId());
        dto.setNombre(asignatura.getNombre());
        dto.setAreaId(asignatura.getArea().getId());
        return dto;
    }

    private Asignatura convertToEntity(AsignaturaDTO dto) {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(dto.getId());
        asignatura.setNombre(dto.getNombre());
        
        Area area = areaRepository.findById(dto.getAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));
        asignatura.setArea(area);
        
        return asignatura;
    }
} 