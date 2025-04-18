package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.AsignacionTutor;
import com.seibe.docente.domain.entity.Docente;
import com.seibe.docente.domain.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionTutorRepository extends JpaRepository<AsignacionTutor, Long> {
    Optional<AsignacionTutor> findByOfertaIdAndEstadoTrue(Long ofertaId);
    boolean existsByOfertaIdAndEstadoTrue(Long ofertaId);
    List<AsignacionTutor> findByDocenteAndEstadoTrue(Docente docente);
    List<AsignacionTutor> findByOfertaAndEstadoTrue(Oferta oferta);
    boolean existsByDocenteIdAndEstadoTrue(Long docenteId);
} 