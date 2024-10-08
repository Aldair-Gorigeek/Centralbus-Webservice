package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.Transacciones;

@Repository
public interface TransaccionRepository extends JpaRepository<Transacciones, Long> {

    Transacciones findByNumTransaccion(String idTransaccion);

    boolean existsByNumTransaccion(String idTransaccion);

}
