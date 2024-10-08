package com.gorigeek.springboot.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.entity.DetalleNotificacionMovil;
import com.gorigeek.springboot.notificaciones.entity.T_notificaciones_push;

@Transactional
public interface NotificacionesRepository extends JpaRepository<T_notificaciones_push, Integer> {

    @Query(value = "SELECT * FROM  t_notificaciones_push t WHERE t.c_dirigido = :tpNoti", nativeQuery = true)
    List<T_notificaciones_push> findByTpNoti(@Param("tpNoti") int tpNoti);

    @Query(value = "SELECT * FROM  t_notificaciones_push t WHERE t.idt_notificaciones_push = :idNoti AND t.c_status_notificacion = 2 AND t.c_estatus_envio = 2", nativeQuery = true)
    List<T_notificaciones_push> findByIdNoti(@Param("idNoti") int idNoti);

    @Query(value = "SELECT * FROM t_notificaciones_push t WHERE t.c_estatus_envio = 2 AND t.c_status_notificacion = 1", nativeQuery = true)
    List<T_notificaciones_push> findNotifications();
    @Modifying
    @Query(value = "UPDATE t_notificaciones_push t SET t.c_estatus_envio = 1 WHERE t.idt_notificaciones_push = :idNoti", nativeQuery = true)
    public int updateNotification(@Param("idNoti") int idNoti);

}
