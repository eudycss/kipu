package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo, Long> {
    Optional<Periodo> findByNombre(String nombre);
    Optional<Periodo> findByActivo(Boolean activo);
    
    // Métodos adicionales
    Optional<Periodo> findByActivoTrue();
    boolean existsByNombre(String nombre);
} 