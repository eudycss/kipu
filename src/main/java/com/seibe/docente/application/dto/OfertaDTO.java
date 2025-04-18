package com.seibe.docente.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDTO {
    private Long ofertaId;
    private String ofeCurso;
    private String ofeParalelo;
    private Integer ofeAforo;
    private Boolean ofeEstado;
    private GradoSimpleDTO grado;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradoSimpleDTO {
        private Long gradoId;
        private String descripcion;
        private String nemonico;
    }
} 