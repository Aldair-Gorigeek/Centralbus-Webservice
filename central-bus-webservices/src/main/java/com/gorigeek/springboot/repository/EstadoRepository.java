package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorigeek.springboot.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long>{

    List<Estado> findByDescripcionContaining(String lugar);

	
}
