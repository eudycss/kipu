package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proceso_educativo")
public class ProcesoEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pred_id")
    private Long id;

    @Column(name = "pred_descripcion", nullable = false)
    private String predDescripcion;

    @Column(name = "pred_nemonico", nullable = false)
    private String predNemonico;

    @Column(name = "pred_estado", nullable = false)
    private String predEstado;

    @Column(name = "pred_nivel", nullable = false)
    private String predNivel;

    @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL)
    private List<Grado> grados = new ArrayList<>();
} 