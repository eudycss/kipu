package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.GradoDTO;
import java.util.List;

public interface GradoService {
    List<GradoDTO> findAll();
    GradoDTO findById(Long id);
    GradoDTO findByNombre(String nombre);
    List<GradoDTO> findByNivel(String nivel);
    GradoDTO create(GradoDTO gradoDTO);
    GradoDTO update(Long id, GradoDTO gradoDTO);
    void delete(Long id);
} 