package com.seibe.docente.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionDocenteListResponse {
    private Long asignacionId;
    private String docenteNombres;
    private String docenteApellidos;
    private String docenteCedula;
    private String asignatura;
    private String curso;
    private String paralelo;
    private Integer horasSemanales = 0;
    private boolean activo;
} 