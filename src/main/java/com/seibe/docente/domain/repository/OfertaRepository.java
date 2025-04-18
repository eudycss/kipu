package com.seibe.docente.domain.repository;

import com.seibe.docente.domain.entity.Oferta;
import com.seibe.docente.domain.entity.Periodo;
import com.seibe.docente.domain.entity.ProcesoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    @Query("SELECT o FROM Oferta o JOIN FETCH o.grado g LEFT JOIN FETCH g.proceso WHERE g.proceso.id = :procesoId AND o.periodo.id = :periodoId")
    List<Oferta> findByGradoProcesoIdAndPeriodoId(@Param("procesoId") Long procesoId, @Param("periodoId") Long periodoId);
    
    List<Oferta> findByGradoProcesoAndPeriodo(ProcesoEducativo proceso, Periodo periodo);
} 