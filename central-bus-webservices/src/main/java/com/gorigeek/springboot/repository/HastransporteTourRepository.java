package com.gorigeek.springboot.repository;

import com.gorigeek.springboot.entity.TourHasTransporte;
import com.gorigeek.springboot.entity.TourMovil;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HastransporteTourRepository extends JpaRepository<TourHasTransporte, Long>{

    //List<TourHasTransporte> findByIdtours(Long idOrigen, Long idDestino);
    
    List<TourHasTransporte> findByIdtour(TourMovil idTour);

    

}
