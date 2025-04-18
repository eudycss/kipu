package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.AreaDTO;
import com.seibe.docente.application.service.AreaService;
import com.seibe.docente.domain.entity.Area;
import com.seibe.docente.domain.repository.AreaRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AreaDTO> findAll() {
        return areaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDTO findById(Long id) {
        return areaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDTO findByNombre(String nombre) {
        return areaRepository.findByNombre(nombre)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));
    }

    @Override
    public AreaDTO create(AreaDTO areaDTO) {
        if (areaRepository.existsByNombre(areaDTO.getNombre())) {
            throw new ResourceNotFoundException("Ya existe un área con este nombre");
        }
        return convertToDTO(areaRepository.save(convertToEntity(areaDTO)));
    }

    @Override
    public AreaDTO update(Long id, AreaDTO areaDTO) {
        Area existingArea = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));

        if (!existingArea.getNombre().equals(areaDTO.getNombre()) &&
            areaRepository.existsByNombre(areaDTO.getNombre())) {
            throw new ResourceNotFoundException("Ya existe un área con este nombre");
        }

        Area area = convertToEntity(areaDTO);
        area.setId(id);
        return convertToDTO(areaRepository.save(area));
    }

    @Override
    public void delete(Long id) {
        if (!areaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Área no encontrada");
        }
        areaRepository.deleteById(id);
    }

    private AreaDTO convertToDTO(Area area) {
        AreaDTO dto = new AreaDTO();
        dto.setId(area.getId());
        dto.setNombre(area.getNombre());
        dto.setEstado(area.getEstado());
        return dto;
    }

    private Area convertToEntity(AreaDTO dto) {
        Area area = new Area();
        area.setId(dto.getId());
        area.setNombre(dto.getNombre());
        area.setEstado(dto.getEstado());
        return area;
    }
} 