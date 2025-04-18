package com.seibe.docente.infrastructure.mapper;

import com.seibe.docente.application.dto.AsignacionDocenteDTO;
import com.seibe.docente.application.dto.AsignacionDocenteListResponse;
import com.seibe.docente.application.dto.OfertaAsignacionDTO;
import com.seibe.docente.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.text.Normalizer;
import java.util.List;

@Mapper(componentModel = "spring", imports = {Normalizer.class})
public interface AsignacionDocenteMapperNew {
    @Mapping(target = "asignacionId", source = "id")
    @Mapping(target = "docenteId", source = "docente.id")
    @Mapping(target = "docenteNombres", expression = "java(normalizeText(asignacion.getDocente() != null ? asignacion.getDocente().getDocNombre() : null))")
    @Mapping(target = "docenteApellidos", constant = "")
    @Mapping(target = "ofertaId", source = "oferta.id")
    @Mapping(target = "curso", source = "oferta.ofeCurso")
    @Mapping(target = "paralelo", source = "oferta.ofeParalelo")
    @Mapping(target = "asignaturaId", source = "asignatura.id")
    @Mapping(target = "asignatura", expression = "java(normalizeText(asignacion.getAsignatura() != null ? asignacion.getAsignatura().getNombre() : null))")
    @Mapping(target = "periodoId", source = "periodo.id")
    @Mapping(target = "periodo", expression = "java(normalizeText(asignacion.getPeriodo() != null ? asignacion.getPeriodo().getNombre() : null))")
    @Mapping(target = "estado", source = "estado")
    AsignacionDocenteDTO toDto(AsignacionDocente asignacion);

    List<AsignacionDocenteDTO> toDtoList(List<AsignacionDocente> asignaciones);

    @Mapping(target = "id", source = "asignacionId")
    @Mapping(target = "docente", source = "docenteId")
    @Mapping(target = "oferta", source = "ofertaId")
    @Mapping(target = "periodo", expression = "java(mapPeriodo(dto.getPeriodoId()))")
    @Mapping(target = "asignatura", expression = "java(mapAsignatura(dto.getAsignaturaId()))")
    @Mapping(target = "estado", source = "estado")
    AsignacionDocente toEntity(AsignacionDocenteDTO dto);

    default Docente mapDocente(Long id) {
        if (id == null) return null;
        Docente docente = new Docente();
        docente.setId(id);
        return docente;
    }

    default Oferta mapOferta(Long id) {
        if (id == null) return null;
        Oferta oferta = new Oferta();
        oferta.setId(id);
        return oferta;
    }

    default Periodo mapPeriodo(Long id) {
        if (id == null) return null;
        Periodo periodo = new Periodo();
        periodo.setId(id);
        return periodo;
    }

    default Asignatura mapAsignatura(Long id) {
        if (id == null) return null;
        Asignatura asignatura = new Asignatura();
        asignatura.setId(id);
        return asignatura;
    }
    
    // Método helper para normalizar texto con caracteres especiales
    default String normalizeText(String text) {
        if (text == null) return null;
        // NFC normaliza la composición de caracteres
        return Normalizer.normalize(text, Normalizer.Form.NFC);
    }

    @Mapping(target = "asignacionId", source = "id")
    @Mapping(target = "docenteNombres", expression = "java(normalizeText(asignacion.getDocente() != null ? asignacion.getDocente().getDocNombre() : null))")
    @Mapping(target = "docenteApellidos", constant = "")
    @Mapping(target = "docenteCedula", source = "docente.docIdentificacion")
    @Mapping(target = "asignatura", expression = "java(normalizeText(asignacion.getAsignatura() != null ? asignacion.getAsignatura().getNombre() : null))")
    @Mapping(target = "curso", source = "oferta.ofeCurso")
    @Mapping(target = "paralelo", source = "oferta.ofeParalelo")
    @Mapping(target = "horasSemanales", constant = "0") // TODO: Integrar con servicio externo de horas
    @Mapping(target = "activo", source = "estado")
    AsignacionDocenteListResponse toListResponse(AsignacionDocente asignacion);

    List<AsignacionDocenteListResponse> toListResponse(List<AsignacionDocente> asignaciones);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "docente", ignore = true)
    @Mapping(target = "oferta", ignore = true)
    @Mapping(target = "periodo", ignore = true)
    @Mapping(target = "asignatura", ignore = true)
    @Mapping(target = "estado", constant = "true")
    void updateEntity(AsignacionDocenteDTO dto, @MappingTarget AsignacionDocente entity);

    @Mapping(target = "curso", source = "ofeCurso")
    @Mapping(target = "paralelo", source = "ofeParalelo")
    @Mapping(target = "asignatura", expression = "java(normalizeText(oferta.getGrado() != null ? oferta.getGrado().getDescripcion() : null))")
    @Mapping(target = "asignado", expression = "java(false)")
    OfertaAsignacionDTO toOfertaAsignacionDTO(Oferta oferta);
} 