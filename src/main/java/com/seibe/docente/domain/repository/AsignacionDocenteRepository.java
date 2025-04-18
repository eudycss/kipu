package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {
    List<AsignacionDocente> findByDocenteId(Long docenteId);
    List<AsignacionDocente> findByOfertaId(Long ofertaId);
    List<AsignacionDocente> findByAsignaturaId(Long asignaturaId);
    boolean existsByOfertaIdAndAsignaturaId(Long ofertaId, Long asignaturaId);
    boolean existsByDocenteIdAndOfertaId(Long docenteId, Long ofertaId);
} 