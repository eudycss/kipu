package com.seibe.docente.application.service.impl;

import com.seibe.docente.application.dto.*;
import com.seibe.docente.infrastructure.mapper.AsignacionDocenteMapper;
import com.seibe.docente.application.service.AsignacionDocenteService;
import com.seibe.docente.domain.entity.*;
import com.seibe.docente.domain.repository.*;
import com.seibe.docente.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AsignacionDocenteServiceImpl implements AsignacionDocenteService {

    // Inyección de configuración de asignaturas por proceso
    @Value("${seibe.asignaturas.ips}")
    private String asignaturasIps;

    @Value("${seibe.asignaturas.fcap}")
    private String asignaturasFcap;

    @Value("${seibe.asignaturas.ddte}")
    private String asignaturasDdte;

    @Value("${seibe.asignaturas.pai}")
    private String asignaturasPai;

    private final AsignacionDocenteRepository asignacionDocenteRepository;
    private final OfertaRepository ofertaRepository;
    private final DocenteRepository docenteRepository;
    private final PeriodoRepository periodoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final AsignacionDocenteMapper mapper;

    // Método para normalizar texto: elimina acentos y pasa a minúsculas
    private String normalizar(String texto) {
        if (texto == null) return "";
        // Normaliza el texto a la forma NFD y elimina los caracteres diacríticos
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
            .toLowerCase();
        return normalizado;
    }
    
    // Método para verificar si una asignatura coincide con alguno de los nombres especificados
    private boolean coincideConNombres(Asignatura asignatura, String... nombres) {
        String nombreNormalizado = normalizar(asignatura.getNombre());
        return Arrays.stream(nombres)
            .map(this::normalizar)
            .anyMatch(nombre -> nombreNormalizado.contains(nombre));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfertaProcesoDTO> getOfertasPorProceso(Long procesoId, Long periodoId) {
        // Verificar que el periodo existe
        Periodo periodo = periodoRepository.findById(periodoId)
            .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado"));
            
        // Obtener todas las ofertas que coincidan con el proceso y periodo
        List<Oferta> ofertas = ofertaRepository.findByGradoProcesoIdAndPeriodoId(procesoId, periodoId);
        
        // Convertir a DTOs aplicando las reglas de negocio
        return ofertas.stream()
            .map(oferta -> construirOfertaProcesoDTO(oferta, procesoId))
            .collect(Collectors.toList());
    }
    
    // Método auxiliar para construir el DTO completo
    private OfertaProcesoDTO construirOfertaProcesoDTO(Oferta oferta, Long procesoId) {
        OfertaProcesoDTO dto = new OfertaProcesoDTO();
        
        // Datos básicos de la oferta
        dto.setOfeId(oferta.getId());
        dto.setCurso(oferta.getOfeCurso());
        dto.setParalelo(oferta.getOfeParalelo());
        
        // Datos del grado - Mapeo completo
        Grado grado = oferta.getGrado();
        GradoDTO gradoDTO = new GradoDTO();
        gradoDTO.setGraId(grado.getId());
        
        // Normalizar la descripción para evitar problemas de codificación
        String descripcion = grado.getDescripcion();
        if (descripcion != null) {
            descripcion = Normalizer.normalize(descripcion, Normalizer.Form.NFC);
        }
        gradoDTO.setDescripcion(descripcion);
        
        // Completar los demás campos
        gradoDTO.setNemonico(grado.getNemonico());
        gradoDTO.setEstado(grado.getEstado());
        
        // Obtener el ID del proceso
        if (grado.getProceso() != null) {
            gradoDTO.setProcesoId(grado.getProceso().getId());
        } else {
            // Si no está disponible en la relación, usamos el procesoId pasado como parámetro
            gradoDTO.setProcesoId(procesoId);
        }
        
        dto.setGrado(gradoDTO);
        
        // Obtener las asignaturas según el proceso educativo
        List<Asignatura> asignaturasFiltradas = filtrarAsignaturasPorProceso(procesoId);
        
        // Para cada asignatura, verificar si ya está asignada a esta oferta
        for (Asignatura asignatura : asignaturasFiltradas) {
            boolean asignado = asignacionDocenteRepository.existsByOfertaIdAndAsignaturaId(
                oferta.getId(), asignatura.getId());
                
            AsignaturaItemDTO asignaturaDTO = new AsignaturaItemDTO();
            asignaturaDTO.setAsiId(asignatura.getId());
            asignaturaDTO.setNombre(asignatura.getNombre());
            asignaturaDTO.setAsignado(asignado);
            
            dto.agregarAsignatura(asignaturaDTO);
        }
        
        return dto;
    }
    
    // Método auxiliar para filtrar asignaturas según el proceso educativo
    private List<Asignatura> filtrarAsignaturasPorProceso(Long procesoId) {
        // Obtener los nemónicos configurados según el proceso
        String configuracion = obtenerConfiguracionPorProceso(procesoId);
        
        // Si la configuración es ALL, devolver todas las asignaturas
        if ("ALL".equals(configuracion)) {
            return asignaturaRepository.findAll();
        }
        
        // Dividir la cadena en nemónicos individuales
        String[] nemonicos = configuracion.split(",");
        
        // Buscar asignaturas por nemónicos
        List<Asignatura> resultado = asignaturaRepository.findByNemonicoIn(Arrays.asList(nemonicos));
        
        log.debug("Asignaturas para proceso {}: {}", procesoId, 
                resultado.stream()
                        .map(a -> a.getNombre())
                        .collect(Collectors.joining(", ")));
        
        return resultado;
    }
    
    // Método para obtener la configuración según el proceso
    private String obtenerConfiguracionPorProceso(Long procesoId) {
        if (procesoId == 1) return asignaturasIps;
        if (procesoId == 2) return asignaturasFcap;
        if (procesoId == 3) return asignaturasDdte;
        if (procesoId == 4) return asignaturasPai;
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocenteListResponse> getAllAsignaciones() {
        List<AsignacionDocente> asignaciones = asignacionDocenteRepository.findAll();
        return mapper.toListResponse(asignaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public AsignacionDocenteDTO findById(Long id) {
        AsignacionDocente asignacion = asignacionDocenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de docente no encontrada con ID: " + id));
        return mapper.toDto(asignacion);
    }

    @Override
    public AsignacionDocenteDTO saveAsignacion(AsignacionDocenteDTO asignacionDTO) {
        Docente docente = docenteRepository.findById(asignacionDTO.getDocenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
        Oferta oferta = ofertaRepository.findById(asignacionDTO.getOfertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        Asignatura asignatura = asignaturaRepository.findById(asignacionDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));
        Periodo periodo = periodoRepository.findById(asignacionDTO.getPeriodoId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado"));
                
        AsignacionDocente asignacion = new AsignacionDocente();
        asignacion.setDocente(docente);
        asignacion.setOferta(oferta);
        asignacion.setAsignatura(asignatura);
        asignacion.setPeriodo(periodo);
        asignacion.setEstado(asignacionDTO.getEstado() != null ? asignacionDTO.getEstado() : true);
        
        AsignacionDocente savedAsignacion = asignacionDocenteRepository.save(asignacion);
        return mapper.toDto(savedAsignacion);
    }

    @Override
    public AsignacionDocenteDTO updateAsignacion(Long id, AsignacionDocenteDTO asignacionDTO) {
        AsignacionDocente asignacion = asignacionDocenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada"));
                
        Docente docente = docenteRepository.findById(asignacionDTO.getDocenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));
        Oferta oferta = ofertaRepository.findById(asignacionDTO.getOfertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));
        Asignatura asignatura = asignaturaRepository.findById(asignacionDTO.getAsignaturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));
        Periodo periodo = periodoRepository.findById(asignacionDTO.getPeriodoId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo no encontrado"));
        
        asignacion.setDocente(docente);
        asignacion.setOferta(oferta);
        asignacion.setAsignatura(asignatura);
        asignacion.setPeriodo(periodo);
        asignacion.setEstado(asignacionDTO.getEstado() != null ? asignacionDTO.getEstado() : asignacion.getEstado());
        
        AsignacionDocente savedAsignacion = asignacionDocenteRepository.save(asignacion);
        return mapper.toDto(savedAsignacion);
    }

    @Override
    public void deleteAsignacion(Long id) {
        AsignacionDocente asignacion = asignacionDocenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada"));
        asignacionDocenteRepository.delete(asignacion);
    }
}