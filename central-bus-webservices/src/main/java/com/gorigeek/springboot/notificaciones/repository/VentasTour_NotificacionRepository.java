package com.gorigeek.springboot.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.notificaciones.entity.Detalle_venta_tour;

public interface VentasTour_NotificacionRepository extends JpaRepository<Detalle_venta_tour, Integer> {

    @Query(value = "SELECT * FROM detalle_venta_tour d WHERE d.t_usuarios_final = :idUser", nativeQuery = true)
    List<Detalle_venta_tour> findByUserTour(@Param("idUser") String idUser);

}
