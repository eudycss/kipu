package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaProcesoDTO {
    private Long ofeId;
    private String curso;
    private String paralelo;
    private GradoDTO grado;
    private List<AsignaturaItemDTO> asignaturas = new ArrayList<>();
    
    // Método útil para agregar asignaturas
    public void agregarAsignatura(AsignaturaItemDTO asignatura) {
        this.asignaturas.add(asignatura);
    }
} 