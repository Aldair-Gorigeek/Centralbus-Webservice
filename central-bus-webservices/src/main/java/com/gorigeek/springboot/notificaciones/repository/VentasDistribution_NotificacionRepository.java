package com.gorigeek.springboot.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.notificaciones.entity.T_ventas_distribusion;

public interface VentasDistribution_NotificacionRepository extends JpaRepository<T_ventas_distribusion, Integer> {

    @Query(value = "SELECT * FROM t_ventas_distribusion t WHERE t.t_usuarios_final = :idUser", nativeQuery = true)
    List<T_ventas_distribusion> findByUserSaleDistribution(@Param("idUser") String idUser);

}
