package com.gorigeek.springboot.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.notificaciones.entity.T_usuarios_final;


public interface TokenUsuario_NotificacionRepository extends JpaRepository<T_usuarios_final, Integer> {
    
    @Query(value = "SELECT * FROM t_usuarios_final u WHERE FIND_IN_SET(u.idt_usuarios_final, :idUser)  AND u.token IS NOT NULL", nativeQuery = true)    
    List<T_usuarios_final> getTokenUser(@Param("idUser") String idUser);
    
    @Query(value = "SELECT * FROM t_usuarios_final t WHERE t.fechaRegistro BETWEEN CURDATE() - INTERVAL 3 MONTH AND CURDATE() AND t.token IS NOT NULL", nativeQuery = true)    
    List<T_usuarios_final> getNewUser();    
            
    @Query(value = "SELECT * FROM t_usuarios_final u WHERE u.estatus_activo = 1 AND u.token IS NOT NULL", nativeQuery = true)    
    List<T_usuarios_final> getAllTokenUser();            

}




