package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.GradoDTO;
import com.seibe.docente.application.service.GradoService;
import com.seibe.docente.domain.entity.Grado;
import com.seibe.docente.domain.entity.ProcesoEducativo;
import com.seibe.docente.domain.repository.GradoRepository;
import com.seibe.docente.domain.repository.ProcesoEducativoRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GradoServiceImpl implements GradoService {
    private final GradoRepository gradoRepository;
    private final ProcesoEducativoRepository procesoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GradoDTO> findAll() {
        return gradoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GradoDTO findById(Long id) {
        return gradoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public GradoDTO findByNombre(String nombre) {
        return gradoRepository.findByDescripcion(nombre)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradoDTO> findByNivel(String nivel) {
        return gradoRepository.findByProcesoPredNivel(nivel).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GradoDTO create(GradoDTO gradoDTO) {
        if (gradoRepository.existsByDescripcion(gradoDTO.getDescripcion())) {
            throw new RuntimeException("Ya existe un grado con esta descripción");
        }
        return convertToDTO(gradoRepository.save(convertToEntity(gradoDTO)));
    }

    @Override
    public GradoDTO update(Long id, GradoDTO gradoDTO) {
        Grado existingGrado = gradoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        if (!existingGrado.getDescripcion().equals(gradoDTO.getDescripcion()) &&
            gradoRepository.existsByDescripcion(gradoDTO.getDescripcion())) {
            throw new RuntimeException("Ya existe un grado con esta descripción");
        }

        Grado grado = convertToEntity(gradoDTO);
        grado.setId(id);
        return convertToDTO(gradoRepository.save(grado));
    }

    @Override
    public void delete(Long id) {
        if (!gradoRepository.findById(id).isPresent()) {
            throw new RuntimeException("Grado no encontrado");
        }
        gradoRepository.deleteById(id);
    }

    private GradoDTO convertToDTO(Grado grado) {
        GradoDTO dto = new GradoDTO();
        dto.setGraId(grado.getId());
        dto.setDescripcion(grado.getDescripcion());
        dto.setNemonico(grado.getNemonico());
        dto.setEstado(grado.getEstado());
        if (grado.getProceso() != null) {
            dto.setProcesoId(grado.getProceso().getId());
        }
        return dto;
    }

    private Grado convertToEntity(GradoDTO dto) {
        Grado grado = new Grado();
        grado.setId(dto.getGraId());
        grado.setDescripcion(dto.getDescripcion());
        grado.setNemonico(dto.getNemonico());
        grado.setEstado(dto.getEstado());
        
        ProcesoEducativo proceso = procesoRepository.findById(dto.getProcesoId())
                .orElseThrow(() -> new ResourceNotFoundException("Proceso educativo no encontrado"));
        grado.setProceso(proceso);
        
        return grado;
    }
} 