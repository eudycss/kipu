package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionTutorDTO {
    private Long asignacionId;
    private Long docenteId;
    private String docenteNombre;
    private Long ofertaId;
    private String ofertaCurso;
    private String ofertaParalelo;
    private String gradoDescripcion;
    private LocalDateTime fechaAsignacion;
    private Boolean estado;
} 