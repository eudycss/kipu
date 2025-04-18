package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.Area;
import com.seibe.docente.domain.entity.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    Optional<Asignatura> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Asignatura> findByNemonicoIn(List<String> nemonicos);
    
    // Métodos para Area
    List<Asignatura> findByArea(Area area);
    Optional<Asignatura> findByNombreAndArea(String nombre, Area area);
    boolean existsByNombreAndArea(String nombre, Area area);
    Optional<Asignatura> findByNemonico(String nemonico);
} 