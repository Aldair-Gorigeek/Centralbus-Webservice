package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.TipoBoleto;

@Repository
public interface TipoBoletoRepository extends JpaRepository<TipoBoleto, Long>{

}
