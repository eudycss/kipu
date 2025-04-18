package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.AreaDTO;
import java.util.List;

public interface AreaService {
    List<AreaDTO> findAll();
    AreaDTO findById(Long id);
    AreaDTO findByNombre(String nombre);
    AreaDTO create(AreaDTO areaDTO);
    AreaDTO update(Long id, AreaDTO areaDTO);
    void delete(Long id);
} 