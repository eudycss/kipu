package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.ProcesoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcesoEducativoRepository extends JpaRepository<ProcesoEducativo, Long> {
    Optional<ProcesoEducativo> findByPredNemonico(String nemonico);
    boolean existsByPredNemonico(String nemonico);
    boolean existsByPredDescripcion(String descripcion);
} 