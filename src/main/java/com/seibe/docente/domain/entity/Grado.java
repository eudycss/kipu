package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "grado")
public class Grado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gra_id")
    private Long id;

    @Column(name = "gra_descripcion")
    private String descripcion;

    @Column(name = "gra_nemonico")
    private String nemonico;

    @Column(name = "gra_estado")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pred_id")
    private ProcesoEducativo proceso;
} 