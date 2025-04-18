package com.seibe.docente.infrastructure.presentation.controller;

import com.seibe.docente.application.dto.PeriodoDTO;
import com.seibe.docente.application.service.PeriodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PeriodoController.class)
class PeriodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PeriodoService periodoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PeriodoDTO periodoDTO;
    private List<PeriodoDTO> periodoDTOList;

    @BeforeEach
    void setUp() {
        periodoDTO = new PeriodoDTO();
        periodoDTO.setId(1L);
        periodoDTO.setNombre("2024-1");
        periodoDTO.setFechaInicio(LocalDate.of(2024, 1, 1));
        periodoDTO.setFechaFin(LocalDate.of(2024, 6, 30));
        periodoDTO.setActivo(true);

        PeriodoDTO periodoDTO2 = new PeriodoDTO();
        periodoDTO2.setId(2L);
        periodoDTO2.setNombre("2024-2");
        periodoDTO2.setFechaInicio(LocalDate.of(2024, 7, 1));
        periodoDTO2.setFechaFin(LocalDate.of(2024, 12, 31));
        periodoDTO2.setActivo(false);

        periodoDTOList = Arrays.asList(periodoDTO, periodoDTO2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_ShouldReturnListOfPeriodos() throws Exception {
        when(periodoService.findAll()).thenReturn(periodoDTOList);

        mockMvc.perform(get("/api/periodos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("2024-1"))
                .andExpect(jsonPath("$[1].nombre").value("2024-2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_ShouldReturnPeriodo() throws Exception {
        when(periodoService.findById(1L)).thenReturn(periodoDTO);

        mockMvc.perform(get("/api/periodos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("2024-1"))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_ShouldReturnCreatedPeriodo() throws Exception {
        when(periodoService.create(any(PeriodoDTO.class))).thenReturn(periodoDTO);

        mockMvc.perform(post("/api/periodos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(periodoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("2024-1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnUpdatedPeriodo() throws Exception {
        when(periodoService.update(eq(1L), any(PeriodoDTO.class))).thenReturn(periodoDTO);

        mockMvc.perform(put("/api/periodos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(periodoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("2024-1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/periodos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "DOCENTE")
    void findPeriodoActivo_ShouldReturnActivePeriodo() throws Exception {
        when(periodoService.findPeriodoActivo()).thenReturn(periodoDTO);

        mockMvc.perform(get("/api/periodos/activo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("2024-1"))
                .andExpect(jsonPath("$.activo").value(true));
    }
} 