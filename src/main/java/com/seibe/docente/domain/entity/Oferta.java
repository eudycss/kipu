package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oferta")
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ofe_id")
    private Long id;

    @Column(name = "ofe_curso", nullable = false)
    private String ofeCurso;

    @Column(name = "ofe_paralelo", nullable = false)
    private String ofeParalelo;

    @Column(name = "ofe_aforo", nullable = false)
    private Integer ofeAforo;

    @Column(name = "ofe_estado", nullable = false)
    private Boolean ofeEstado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pele_id", nullable = false)
    private Periodo periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gra_id", nullable = false)
    private Grado grado;
} 