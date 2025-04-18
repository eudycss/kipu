package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asignatura")
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asi_id")
    private Long id;

    @Column(name = "asi_nombre", nullable = false)
    private String nombre;

    @Column(name = "asi_nemonico", nullable = false)
    private String nemonico;

    @Column(name = "asi_estado", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ar_id", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL)
    private Set<AsignacionDocente> asignaciones = new HashSet<>();
} 