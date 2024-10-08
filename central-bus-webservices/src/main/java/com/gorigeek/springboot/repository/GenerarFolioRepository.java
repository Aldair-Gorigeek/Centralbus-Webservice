package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.entity.DetalleVentasModel;
import com.gorigeek.springboot.entity.TAfiliado;
import com.gorigeek.springboot.entity.TUsuariosAdmin;


public interface GenerarFolioRepository extends JpaRepository<DetalleVentasModel, String> {

    @Modifying
    @Query(value = "SELECT * FROM detalle_venta d "
            + "INNER JOIN t_venta_viajes t ON t.idt_venta_viajes = d.t_venta_viajes " + "WHERE t.t_afiliado = ?1 "
            + "GROUP BY d.id_detalle_venta " + "ORDER by d.id_detalle_venta DESC LIMIT 1", nativeQuery = true)
    public List<DetalleVentasModel> obtenerUltimoFolio(String idAfiliado);

    @Modifying
    @Query(value = "SELECT t FROM TAfiliado t WHERE t.idt_afiliado = ?1")
    public List<TAfiliado> obtenerAfiliado(String id);

    @Modifying
    @Query(value = "SELECT t FROM TUsuariosAdmin t WHERE t.idt_usuarios_final = ?1")
    public List<TUsuariosAdmin> obtenerUsuario(String id);

    @Modifying
    @Query(value = "INSERT INTO t_generador_folios (t_usuarios_final,t_afiliado,folio,dv_uno,"
            + "dv_dos,fecha_viaje,fecha_generacion,t_autobus,tipo) VALUES "
            + "(:t_usuarios_final,:t_afiliado,:folio,:dv_uno,:dv_dos,:fecha_viaje,NOW(),:t_autobus,:tipo)", nativeQuery = true)
    public int insertFolio(@Param("t_usuarios_final") Integer t_usuarios_final, @Param("t_afiliado") Integer t_afiliado,
            @Param("folio") String folio, @Param("dv_uno") String dv_uno, @Param("dv_dos") String dv_dos,
            @Param("fecha_viaje") String fecha_viaje, @Param("t_autobus") Integer t_autobus,
            @Param("tipo") String tipo);

    @Modifying
    @Query(value = "INSERT INTO t_generador_folios (t_afiliado,folio,dv_uno,"
            + "dv_dos,fecha_viaje,fecha_generacion,t_autobus,tipo) VALUES "
            + "(:t_afiliado,:folio,:dv_uno,:dv_dos,:fecha_viaje,NOW(),:t_autobus,:tipo)", nativeQuery = true)
    public int insertFolioSinUsuario(@Param("t_afiliado") Integer t_afiliado, @Param("folio") String folio,
            @Param("dv_uno") String dv_uno, @Param("dv_dos") String dv_dos, @Param("fecha_viaje") String fecha_viaje,
            @Param("t_autobus") Integer t_autobus, @Param("tipo") String tipo);

}
