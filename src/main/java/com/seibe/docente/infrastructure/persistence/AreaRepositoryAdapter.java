package com.seibe.docente.infrastructure.persistence;

import com.seibe.docente.domain.entity.Area;
import com.seibe.docente.domain.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AreaRepositoryAdapter implements AreaRepository {
    private final SpringDataAreaRepository springDataAreaRepository;

    @Override
    public List<Area> findAll() {
        return springDataAreaRepository.findAll();
    }

    @Override
    public Optional<Area> findById(Long id) {
        return springDataAreaRepository.findById(id);
    }

    @Override
    public Optional<Area> findByNombre(String nombre) {
        return springDataAreaRepository.findByNombre(nombre);
    }

    @Override
    public Area save(Area area) {
        return springDataAreaRepository.save(area);
    }

    @Override
    public void deleteById(Long id) {
        springDataAreaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return springDataAreaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataAreaRepository.existsById(id);
    }
} 