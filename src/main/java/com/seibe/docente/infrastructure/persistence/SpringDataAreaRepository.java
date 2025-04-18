package com.seibe.docente.infrastructure.persistence;

import com.seibe.docente.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataAreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
} 