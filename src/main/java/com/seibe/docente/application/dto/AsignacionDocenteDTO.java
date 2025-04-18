package com.seibe.docente.application.dto;

import lombok.Data;

@Data
public class AsignacionDocenteDTO {
    private Long asignacionId;
    private Long docenteId;
    private String docenteNombres;
    private String docenteApellidos;
    private Long ofertaId;
    private String curso;
    private String paralelo;
    private Long asignaturaId;
    private String asignatura;
    private Long periodoId;
    private String periodo;
    private Boolean estado;
} 