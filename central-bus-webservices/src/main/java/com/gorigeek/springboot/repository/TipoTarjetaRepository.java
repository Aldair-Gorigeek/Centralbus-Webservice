package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.TipoTarjeta;

@Repository
public interface TipoTarjetaRepository  extends JpaRepository<TipoTarjeta, Long>{
    

}
