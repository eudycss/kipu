package com.seibe.docente.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AsignaturaDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    @NotNull(message = "El ID del área es obligatorio")
    private Long areaId;
} 