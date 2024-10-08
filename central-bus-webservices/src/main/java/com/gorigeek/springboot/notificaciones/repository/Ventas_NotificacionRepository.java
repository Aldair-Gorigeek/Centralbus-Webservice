package com.gorigeek.springboot.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.notificaciones.entity.Detalle_venta;

public interface Ventas_NotificacionRepository extends JpaRepository<Detalle_venta, Integer> {

    @Query(value = "SELECT * FROM detalle_venta d where d.t_usuarios_final = :idUser", nativeQuery = true)
    List<Detalle_venta> findByUserSale(@Param("idUser") String idUser);

}
