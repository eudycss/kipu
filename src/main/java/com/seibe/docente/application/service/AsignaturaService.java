package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.AsignaturaDTO;
import java.util.List;

public interface AsignaturaService {
    List<AsignaturaDTO> findAll();
    AsignaturaDTO findById(Long id);
    List<AsignaturaDTO> findByAreaId(Long areaId);
    AsignaturaDTO findByNombreAndAreaId(String nombre, Long areaId);
    AsignaturaDTO create(AsignaturaDTO asignaturaDTO);
    AsignaturaDTO update(Long id, AsignaturaDTO asignaturaDTO);
    void delete(Long id);
} 