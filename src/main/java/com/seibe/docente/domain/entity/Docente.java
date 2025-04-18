package com.seibe.docente.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "docente")
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long id;

    @Column(name = "doc_identificacion")
    private String docIdentificacion;

    @Column(name = "doc_nombre")
    private String docNombre;

    @Column(name = "ins_id")
    private Long institucionId;

    @Column(name = "doc_estado")
    private Boolean docEstado;

    @Column(name = "doc_correo")
    private String docCorreo;

    @Column(name = "doc_telefono")
    private String docTelefono;

    @Column(name = "ar_id")
    private Long areaId;
} 