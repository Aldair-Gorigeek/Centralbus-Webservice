package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorigeek.springboot.entity.Disponibilidad;

public interface StatusDisponibilidadRepository extends JpaRepository<Disponibilidad, Long>{

}
