package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Long> {
    Optional<Grado> findByDescripcion(String descripcion);
    boolean existsByDescripcion(String descripcion);
    List<Grado> findByProcesoPredNivel(String nivel);
} 