package com.gorigeek.springboot.distribution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorigeek.springboot.distribution.entity.VentasDistribution;
import com.gorigeek.springboot.entity.UserFinal;

public interface VentasRepository extends JpaRepository<VentasDistribution, Long> {
    List<VentasDistribution> findByIdUsuarioFinal(Long idUsuarioFinal);

    List<VentasDistribution> findByIdReservacion(String idReservacion);

}
