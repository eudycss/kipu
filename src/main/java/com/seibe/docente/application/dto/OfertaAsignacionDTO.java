package com.seibe.docente.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaAsignacionDTO {
    private Long id;
    private String curso;
    private String paralelo;
    private String asignatura;
    private Integer horasSemanales;
    private boolean asignado;
} 