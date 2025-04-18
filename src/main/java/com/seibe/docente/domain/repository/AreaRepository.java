package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.Area;
import java.util.List;
import java.util.Optional;

public interface AreaRepository {
    List<Area> findAll();
    Optional<Area> findById(Long id);
    Optional<Area> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsById(Long id);
    Area save(Area area);
    void deleteById(Long id);
} 