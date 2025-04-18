package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaResponseDTO {
    private Long asignaturaId;
    private String nombre;
    private String nemonico;
    private Long areaId;
    private String areaNombre;
    private Boolean asignado;
} 