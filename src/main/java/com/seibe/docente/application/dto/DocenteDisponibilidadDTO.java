package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocenteDisponibilidadDTO {
    private Long docenteId;
    private String docIdentificacion;
    private String docNombre;
    private String docCorreo;
    private Long areaId;
    private String areaNombre;
    private Boolean disponible;
} 