package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.entity.DetalleNotificacionMovil;

public interface DetalleNotificacionRepository extends JpaRepository<DetalleNotificacionMovil, Long> {

    @Query(value = "SELECT * FROM detalle_venta WHERE estatus_disponible=1 AND t_usuarios_final IS NOT NULL AND c_estatus_notificacion=1 AND fecha_viaje LIKE :fecha", nativeQuery = true)
    List<DetalleNotificacionMovil> findByStatus(@Param("fecha") String fecha);

}
