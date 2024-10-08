package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.Ciudad;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
	
	List<Ciudad> findByIdEstado(long idEstado);
	
	@Query(value = "SELECT * FROM `c_ciudades`  \r\n"
	        + "WHERE c_estados_idc_estados = ?1 \r\n"
	        + "AND EXISTS(SELECT 1 FROM t_terminales WHERE t_terminales.c_ciudades_idc_ciudades = c_ciudades.idc_ciudades)", nativeQuery = true)
	List<Ciudad> findByIdEstadoAndTerminal(long idEstado);

	Ciudad findById(long idCiudad);
}
