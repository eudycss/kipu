package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asignacion_tutor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ofe_id", "estado"}, name = "uk_asignacion_tutor_oferta_estado")
})
public class AsignacionTutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "astut_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ofe_id", nullable = false)
    private Oferta oferta;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @PrePersist
    protected void onCreate() {
        fechaAsignacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }
} 