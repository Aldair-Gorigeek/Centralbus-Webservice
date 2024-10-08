package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.StatusCuenta;

@Repository
public interface StatusCuentaRepository extends JpaRepository<StatusCuenta, Integer>{

}
