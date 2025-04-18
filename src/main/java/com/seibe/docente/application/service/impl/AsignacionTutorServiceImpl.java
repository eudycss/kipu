package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.AsignacionTutorDTO;
import com.seibe.docente.application.dto.DocenteDTO;
import com.seibe.docente.application.service.AsignacionTutorService;
import com.seibe.docente.domain.entity.AsignacionTutor;
import com.seibe.docente.domain.entity.Docente;
import com.seibe.docente.domain.entity.Oferta;
import com.seibe.docente.domain.repository.AsignacionTutorRepository;
import com.seibe.docente.domain.repository.DocenteRepository;
import com.seibe.docente.domain.repository.OfertaRepository;
import com.seibe.docente.domain.repository.AsignacionDocenteRepository;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AsignacionTutorServiceImpl implements AsignacionTutorService {
    private final AsignacionTutorRepository asignacionTutorRepository;
    private final DocenteRepository docenteRepository;
    private final OfertaRepository ofertaRepository;
    private final AsignacionDocenteRepository asignacionDocenteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionTutorDTO> findAll() {
        return asignacionTutorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AsignacionTutorDTO findById(Long id) {
        return asignacionTutorRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de tutor no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public AsignacionTutorDTO findByOfertaId(Long ofertaId) {
        return asignacionTutorRepository.findByOfertaIdAndEstadoTrue(ofertaId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No hay tutor asignado para esta oferta"));
    }

    @Override
    public AsignacionTutorDTO create(AsignacionTutorDTO dto) {
        // Validar que no exista un tutor para esta oferta
        if (asignacionTutorRepository.existsByOfertaIdAndEstadoTrue(dto.getOfertaId())) {
            throw new IllegalStateException("Ya existe un tutor asignado para esta oferta");
        }
        
        // Obtener docente y verificar que esté activo
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
                
        if (!docente.getDocEstado()) {
            throw new IllegalStateException("No se puede asignar como tutor a un docente inactivo");
        }
        
        // Verificar que el docente no sea tutor de otra oferta
        if (asignacionTutorRepository.existsByDocenteIdAndEstadoTrue(docente.getId())) {
            throw new IllegalStateException("El docente ya es tutor de otra oferta. Un docente solo puede ser tutor de una oferta a la vez.");
        }
        
        // Obtener oferta
        Oferta oferta = ofertaRepository.findById(dto.getOfertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        
        // Crear y configurar la asignación
        AsignacionTutor asignacionTutor = new AsignacionTutor();
        asignacionTutor.setId(dto.getAsignacionId());
        asignacionTutor.setDocente(docente);
        asignacionTutor.setOferta(oferta);
        asignacionTutor.setEstado(true);
        
        return convertToDTO(asignacionTutorRepository.save(asignacionTutor));
    }

    @Override
    public AsignacionTutorDTO update(Long id, AsignacionTutorDTO dto) {
        AsignacionTutor asignacionTutor = asignacionTutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de tutor no encontrada"));
        
        // Si cambia el docente o la oferta, actualizar las referencias
        if (!asignacionTutor.getDocente().getId().equals(dto.getDocenteId())) {
            Docente docente = docenteRepository.findById(dto.getDocenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
            asignacionTutor.setDocente(docente);
        }
        
        if (!asignacionTutor.getOferta().getId().equals(dto.getOfertaId())) {
            Oferta oferta = ofertaRepository.findById(dto.getOfertaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
            asignacionTutor.setOferta(oferta);
        }
        
        asignacionTutor.setEstado(dto.getEstado());
        return convertToDTO(asignacionTutorRepository.save(asignacionTutor));
    }

    @Override
    public void delete(Long id) {
        AsignacionTutor asignacionTutor = asignacionTutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de tutor no encontrada"));
        
        // Desactivar en lugar de eliminar físicamente
        asignacionTutor.setEstado(false);
        asignacionTutorRepository.save(asignacionTutor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionTutorDTO> findByDocenteId(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
        
        return asignacionTutorRepository.findByDocenteAndEstadoTrue(docente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteDTO> findDocentesActivos() {
        return docenteRepository.findByDocEstadoTrue().stream()
                .map(this::convertDocenteToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteDTO findDocenteById(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
        return convertDocenteToDTO(docente);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocenteIdAndOfertaId(Long docenteId, Long ofertaId) {
        // Verificar si existe una asignación docente para este docente y oferta
        return asignacionDocenteRepository.existsByDocenteIdAndOfertaId(docenteId, ofertaId);
    }

    private AsignacionTutorDTO convertToDTO(AsignacionTutor asignacionTutor) {
        AsignacionTutorDTO dto = new AsignacionTutorDTO();
        dto.setAsignacionId(asignacionTutor.getId());
        dto.setDocenteId(asignacionTutor.getDocente().getId());
        dto.setDocenteNombre(asignacionTutor.getDocente().getDocNombre());
        dto.setOfertaId(asignacionTutor.getOferta().getId());
        dto.setOfertaCurso(asignacionTutor.getOferta().getOfeCurso());
        dto.setOfertaParalelo(asignacionTutor.getOferta().getOfeParalelo());
        dto.setGradoDescripcion(asignacionTutor.getOferta().getGrado().getDescripcion());
        dto.setFechaAsignacion(asignacionTutor.getFechaAsignacion());
        dto.setEstado(asignacionTutor.getEstado());
        return dto;
    }

    private AsignacionTutor convertToEntity(AsignacionTutorDTO dto) {
        AsignacionTutor asignacionTutor = new AsignacionTutor();
        asignacionTutor.setId(dto.getAsignacionId());
        
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
        asignacionTutor.setDocente(docente);
        
        Oferta oferta = ofertaRepository.findById(dto.getOfertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        asignacionTutor.setOferta(oferta);
        
        asignacionTutor.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        return asignacionTutor;
    }

    private DocenteDTO convertDocenteToDTO(Docente docente) {
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
} 