package com.gorigeek.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.VentaTourMovil;

@Repository
public interface VentaTourRepository extends JpaRepository<VentaTourMovil, Long>{

    //SELECT idt_venta_tour, t_usuariosadmin_cajero, total_pagado, fecha_compra, t_tour, t_afiliado, promocion 
    //FROM t_venta_tour INNER JOIN detalle_venta_tour ON detalle_venta_tour.t_venta_tour = t_venta_tour.idt_venta_tour
    //WHERE detalle_venta_tour.t_usuarios_final = 2
    
    List<VentaTourMovil> findByDetalleventaTourViajesUsuarioFinal(Long usuarioFinal);
}
