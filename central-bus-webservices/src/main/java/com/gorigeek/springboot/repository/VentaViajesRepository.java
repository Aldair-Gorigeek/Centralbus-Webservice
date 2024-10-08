package com.gorigeek.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.VentaViajesMovil;

@Repository
public interface VentaViajesRepository extends JpaRepository<VentaViajesMovil, Long> {
    
    //SELECT * FROM t_venta_viajes INNER JOIN detalle_venta ON detalle_venta.t_venta_viajes = t_venta_viajes.idt_venta_viajes WHERE detalle_venta.t_usuarios_final = 2

    //SELECT idt_venta_viajes, t_usuariosadmin_cajero, total_pagado, fecha_compra, t_afiliado, t_ruta, t_terminales_origen, t_terminales_destino, promocion 
    //FROM t_venta_viajes INNER JOIN detalle_venta ON detalle_venta.t_venta_viajes = t_venta_viajes.idt_venta_viajes WHERE detalle_venta.t_usuarios_final = 2



    List<VentaViajesMovil> findByDetalleventaViajesUsuarioFinal(Long usuarioFinal);
 


}
