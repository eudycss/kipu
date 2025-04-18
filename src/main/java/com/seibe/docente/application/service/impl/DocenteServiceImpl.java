package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.DocenteDTO;
import com.seibe.docente.application.service.DocenteService;
import com.seibe.docente.domain.entity.Area;
import com.seibe.docente.domain.entity.Docente;
import com.seibe.docente.domain.repository.AreaRepository;
import com.seibe.docente.domain.repository.DocenteRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocenteServiceImpl implements DocenteService {
    private final DocenteRepository docenteRepository;
    private final AreaRepository areaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DocenteDTO> findAll() {
        return docenteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteDTO findById(Long id) {
        return docenteRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteDTO findByCedula(String cedula) {
        return docenteRepository.findByDocIdentificacion(cedula)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
    }

    @Override
    public DocenteDTO create(DocenteDTO docenteDTO) {
        if (docenteRepository.existsByDocIdentificacion(docenteDTO.getDocIdentificacion())) {
            throw new IllegalStateException("Ya existe un docente con la identificación: " + docenteDTO.getDocIdentificacion());
        }
        return convertToDTO(docenteRepository.save(convertToEntity(docenteDTO)));
    }

    @Override
    public DocenteDTO update(Long id, DocenteDTO docenteDTO) {
        Docente existingDocente = docenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + id));
        
        if (!existingDocente.getDocIdentificacion().equals(docenteDTO.getDocIdentificacion()) &&
            docenteRepository.existsByDocIdentificacion(docenteDTO.getDocIdentificacion())) {
            throw new IllegalStateException("Ya existe un docente con la identificación: " + docenteDTO.getDocIdentificacion());
        }

        Docente docente = convertToEntity(docenteDTO);
        docente.setId(id);
        return convertToDTO(docenteRepository.save(docente));
    }

    @Override
    public void delete(Long id) {
        if (!docenteRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Docente no encontrado");
        }
        docenteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteDTO> findByAreaId(Long areaId) {
        return docenteRepository.findByAreaId(areaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteDTO> findAllOrderedByArea(Long areaId) {
        // Verificamos que el área exista
        if (areaId != null && !areaRepository.existsById(areaId)) {
            throw new ResourceNotFoundException("Área no encontrada con ID: " + areaId);
        }
        
        // Obtenemos todos los docentes
        List<Docente> allDocentes = docenteRepository.findAll();
        
        // Preparamos listas separadas para docentes del área especificada y otros docentes
        List<DocenteDTO> docentesInArea = new ArrayList<>();
        List<DocenteDTO> otherDocentes = new ArrayList<>();
        
        // Clasificamos los docentes
        for (Docente docente : allDocentes) {
            DocenteDTO dto = convertToDTO(docente);
            if (areaId != null && docente.getAreaId() != null && docente.getAreaId().equals(areaId)) {
                docentesInArea.add(dto);
            } else {
                otherDocentes.add(dto);
            }
        }
        
        // Combinamos las listas, con los docentes del área especificada primero
        List<DocenteDTO> result = new ArrayList<>(docentesInArea);
        result.addAll(otherDocentes);
        
        return result;
    }

    private DocenteDTO convertToDTO(Docente docente) {
        DocenteDTO dto = new DocenteDTO();
        dto.setDocenteId(docente.getId());
        dto.setDocNombre(docente.getDocNombre());
        dto.setDocIdentificacion(docente.getDocIdentificacion());
        dto.setDocCorreo(docente.getDocCorreo());
        dto.setDocTelefono(docente.getDocTelefono());
        dto.setDocEstado(docente.getDocEstado());
        dto.setInstitucionId(docente.getInstitucionId());
        dto.setAreaId(docente.getAreaId());
        return dto;
    }

    private Docente convertToEntity(DocenteDTO dto) {
        Docente docente = new Docente();
        docente.setId(dto.getDocenteId());
        docente.setDocNombre(dto.getDocNombre());
        docente.setDocIdentificacion(dto.getDocIdentificacion());
        docente.setDocCorreo(dto.getDocCorreo());
        docente.setDocTelefono(dto.getDocTelefono());
        docente.setDocEstado(dto.getDocEstado());
        docente.setInstitucionId(dto.getInstitucionId());
        docente.setAreaId(dto.getAreaId());
        return docente;
    }
} 