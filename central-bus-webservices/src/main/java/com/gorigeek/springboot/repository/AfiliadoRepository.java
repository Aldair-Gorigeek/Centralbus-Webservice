package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorigeek.springboot.entity.Afiliado;

public interface AfiliadoRepository extends JpaRepository<Afiliado, Long> {
	
}
