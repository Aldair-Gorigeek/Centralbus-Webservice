package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.TipoRuta;

@Repository
public interface TipoRutaRepository extends JpaRepository<TipoRuta, Long>{

}
