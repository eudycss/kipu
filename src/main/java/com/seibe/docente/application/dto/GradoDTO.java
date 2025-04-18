package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoDTO {
    private Long graId;
    private String descripcion;
    private String nemonico;
    private String estado;
    private Long procesoId;
} 