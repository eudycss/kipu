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
@Table(name = "area")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ar_id")
    private Long id;

    @Column(name = "ar_nombre", nullable = false)
    private String nombre;

    @Column(name = "ar_estado", nullable = false)
    private Boolean estado;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private Set<Asignatura> asignaturas = new HashSet<>();
} 