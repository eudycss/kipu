package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asignacion_docente")
public class AsignacionDocente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asdo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ofe_id", nullable = false)
    private Oferta oferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pele_id", nullable = false)
    private Periodo periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asi_id", nullable = false)
    private Asignatura asignatura;

    @Column(name = "asdo_estado", nullable = false)
    private Boolean estado = true;
} 