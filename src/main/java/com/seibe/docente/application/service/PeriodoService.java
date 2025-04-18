package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.PeriodoDTO;
import java.util.List;

public interface PeriodoService {
    List<PeriodoDTO> findAll();
    PeriodoDTO findById(Long id);
    PeriodoDTO findByNombre(String nombre);
    PeriodoDTO create(PeriodoDTO periodoDTO);
    PeriodoDTO update(Long id, PeriodoDTO periodoDTO);
    void delete(Long id);
    PeriodoDTO findPeriodoActivo();
    PeriodoDTO activarPeriodo(Long id);
} 