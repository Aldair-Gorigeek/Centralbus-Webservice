package com.gorigeek.springboot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gorigeek.springboot.entity.DetalleNotificacionMovilTour;

public interface DetalleNotificacionTourRepository extends JpaRepository<DetalleNotificacionMovilTour, String> {
    
    @Query(value = "SELECT * FROM detalle_venta_tour WHERE estatus_disponible=1 AND t_usuarios_final IS NOT NULL AND c_estatus_notificacion=1 AND fecha_viaje LIKE :fecha", nativeQuery = true)
    List<DetalleNotificacionMovilTour> findNotificationTour(@Param("fecha") String fecha);

}
