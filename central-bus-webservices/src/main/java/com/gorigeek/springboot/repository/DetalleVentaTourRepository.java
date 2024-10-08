package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.DetalleVentaMovil;
import com.gorigeek.springboot.entity.DetalleVentaTourMovil;

public interface DetalleVentaTourRepository extends JpaRepository<DetalleVentaTourMovil, Long>{

    List<DetalleVentaTourMovil> findByUsuarioFinal(Long idUsuario);

    List<DetalleVentaTourMovil> findByUsuarioFinalAndStatusDisponibleNotOrderByIdDetalleVentaTourDesc(Long idUsuario, int status);

    
    /** metodos para obtener asientos ocupados tour*/
    
    
    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta_tour AS a\r\n"
            + "JOIN t_venta_tour AS b ON a.t_venta_tour = b.idt_venta_tour\r\n"
            + "JOIN t_tour AS c ON b.t_tour = c.idt_tour\r\n"
            + "WHERE a.t_autobus = ?1 \r\n"
            + "AND DATE(fecha_viaje) = DATE(?2) \r\n"
            //+ "#AND TIME(fecha_viaje) = '12:00:00'\r\n"
            //+ "#AND b.t_terminales_origen = ?3 AND b.t_terminales_destino = ?4\r\n"
            + "AND b.t_tour = ?3 \r\n"
            + "AND (a.estatus_disponible = 1 OR a.estatus_disponible = 2)", nativeQuery = true)
    List<DetalleVentaTourMovil> getAsientosOcupados(Long idAutobus, String fecha, int tour);
    //List<DetalleVentaTourMovil> getAsientosOcupados(Long idAutobus, String fecha, int origen, int destino, String hora);

    List<DetalleVentaTourMovil> findByVentaTourIdVentaTour(long parseLong);
    
    /*
    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n"
            + "AND TIME(fecha_viaje)=?5\r\n"           
            + "AND b.t_terminales_origen=?3 AND b.t_terminales_destino<>?4\r\n"
            + "AND a.estatus_disponible=1", nativeQuery = true)
    List<DetalleVentaTourMovil> getAsientosOcupadosInicio(Long idAutobus, String fecha, int origen, int destino, String hora);
    
    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n"
            //+ "AND TIME(fecha_viaje)=?5\r\n"           
            + "AND b.t_terminales_origen<>?3 AND b.t_terminales_destino=?4\r\n"
            + "AND a.estatus_disponible=1", nativeQuery = true)
    List<DetalleVentaTourMovil> getAsientosOcupadosInicioDestino(Long idAutobus, String fecha, int origen, int destino, String hora);
    
    
    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n" 
            + "AND b.t_terminales_origen<>?3 AND b.t_terminales_destino=?4\r\n"
            + "AND a.estatus_disponible=1"
            /* + "AND TIME(fecha_viaje)=?5"/, nativeQuery = true)
    List<DetalleVentaTourMovil> getAsientosOcupadosFinal(Long idAutobus, String fecha, int origen, int destino);   
    
    
    
    @Query(value = "SELECT *\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "LEFT JOIN t_venta_viajes as b\r\n"
            + "ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "WHERE a.t_venta_viajes IN (\r\n"
            + "    SELECT b2.idt_venta_viajes\r\n"
            + "    FROM t_venta_viajes AS b2\r\n"
            + "    WHERE b2.t_ruta=b.t_ruta\r\n"
            + "        AND NOT (b2.t_terminales_origen = ?3 AND b2.t_terminales_destino = ?4)\r\n"
            + "        AND b2.t_terminales_origen <> ?4 AND b2.t_terminales_destino <> ?3\r\n"
            + ")\r\n"
            + "AND b.t_ruta IN(\r\n"
            + "    SELECT b3.t_ruta FROM t_detalle_ruta AS b3 \r\n"
            + "                WHERE  b3.secuencia <= (SELECT secuencia FROM t_detalle_ruta WHERE t_terminales_origen=?3)\r\n"
            + "                AND     b3.secuencia >= (SELECT secuencia FROM t_detalle_ruta WHERE t_terminales_destino=?4)\r\n"
            + ")\r\n"
            + "AND a.t_autobus=?1\r\n"
            + "AND DATE(fecha_viaje)=?2\r\n"
            + "AND a.estatus_disponible=1"
            /*+ "AND TIME(fecha_viaje)=?5"/, nativeQuery = true)
    List<DetalleVentaTourMovil> getAsientosOcupadosMedio(Long idAutobus, String fecha, int origen, int destino);
    */
    /** fin de metodos para obtener asientos ocupados tour*/
}
