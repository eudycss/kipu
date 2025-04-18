package com.seibe.docente.application.service;

import com.seibe.docente.application.dto.DocenteDTO;
import java.util.List;

public interface DocenteService {
    List<DocenteDTO> findAll();
    DocenteDTO findById(Long id);
    DocenteDTO findByCedula(String cedula);
    DocenteDTO create(DocenteDTO docenteDTO);
    DocenteDTO update(Long id, DocenteDTO docenteDTO);
    void delete(Long id);
    List<DocenteDTO> findByAreaId(Long areaId);
    List<DocenteDTO> findAllOrderedByArea(Long areaId);
} 