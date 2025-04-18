package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByDocIdentificacion(String identificacion);
    boolean existsByDocIdentificacion(String identificacion);
    List<Docente> findByAreaId(Long areaId);
    List<Docente> findByDocEstadoTrue();
} 