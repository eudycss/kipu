package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "periodo_lectivo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pele_id")
    private Long id;

    @Column(name = "pele_nombre")
    private String nombre;

    @Column(name = "pele_fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "pele_fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "pele_activo")
    private Boolean activo;

    @Column(name = "pele_estado")
    private Boolean estado;

    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL)
    private Set<Oferta> ofertas = new HashSet<>();

    // Métodos de dominio
    public void activar() {
        this.activo = true;
        this.estado = true;
    }

    public void desactivar() {
        this.activo = false;
        this.estado = false;
    }

    public boolean estaActivo() {
        return this.activo && this.estado;
    }

    public boolean estaEnFecha(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public void agregarOferta(Oferta oferta) {
        ofertas.add(oferta);
        oferta.setPeriodo(this);
    }

    public void removerOferta(Oferta oferta) {
        ofertas.remove(oferta);
        oferta.setPeriodo(null);
    }
} 