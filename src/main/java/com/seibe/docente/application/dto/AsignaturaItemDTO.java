package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaItemDTO {
    private Long asiId;
    private String nombre;
    private boolean asignado;
} 