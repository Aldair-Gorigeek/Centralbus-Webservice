package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.DetalleVentaMovil;

public interface DetalleVentaRepository extends JpaRepository<DetalleVentaMovil, Long> {

    List<DetalleVentaMovil> findByUsuarioFinal(Long idUsuario);

    List<DetalleVentaMovil> findByUsuarioFinalAndStatusDisponibleNotOrderByIdDetalleVentaAsc(Long idUsuario,
            int status);

    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n"
            + "AND TIME(fecha_viaje)=?5\r\n"
            + "AND b.t_terminales_origen=?3 AND b.t_terminales_destino=?4\r\n"
            + "AND (a.estatus_disponible=1 OR a.estatus_disponible=2)", nativeQuery = true)
    List<DetalleVentaMovil> getAsientosOcupados(Long idAutobus, String fecha, int origen, int destino, String hora);
    
    @Query(value = "SELECT b.* \r\n"
            + "FROM t_venta_viajes AS a \r\n"
            + "JOIN detalle_venta AS b ON a.idt_venta_viajes = b.t_venta_viajes \r\n"
            + "JOIN t_ruta AS c ON a.t_ruta = c.idt_ruta \r\n"
            + "JOIN t_afiliado AS af ON af.idt_afiliado =  c.t_afiliado \r\n"
            + "WHERE b.t_autobus = :idAutobus \r\n"
            + "AND DATE(b.fecha_viaje) = :fecha \r\n"
//            + "AND a.t_terminales_destino = :destino \r\n"
            + "AND c.idt_ruta = :ruta \r\n"
            + "AND af.c_statuscuenta_activo IN (1)\r\n"
            + "AND b.estatus_disponible IN (1, 2) FOR UPDATE;", nativeQuery = true)
    List<DetalleVentaMovil> getVentaViaje(Long idAutobus, String fecha, int ruta);
    
    @Query(value = "SELECT DISTINCT b.*\r\n"
            + "FROM t_venta_viajes AS a\r\n"
            + "JOIN detalle_venta AS b ON a.idt_venta_viajes = b.t_venta_viajes \r\n"
            + "JOIN t_ruta AS c ON a.t_ruta = c.idt_ruta\r\n"
            + "JOIN t_afiliado AS af ON af.idt_afiliado =  c.t_afiliado\r\n"
            + "WHERE b.t_autobus = :idAutobus \r\n"
            + "AND (b.fecha_viaje >= :fechaInicio AND b.fecha_viaje <= :fechaFin) \r\n"
            + "AND c.idt_ruta = :ruta \r\n"
            + "AND af.c_statuscuenta_activo IN (1)\r\n"
            + "AND b.estatus_disponible IN (1, 2) FOR UPDATE;", nativeQuery = true)
    List<DetalleVentaMovil> getDetalleVentas(Long idAutobus, String fechaInicio,String fechaFin, int ruta);

    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n"
            + "AND TIME(fecha_viaje)=?5\r\n"
            + "AND b.t_terminales_origen=?3 AND b.t_terminales_destino<>?4\r\n"
            + "AND a.estatus_disponible=1", nativeQuery = true)
    List<DetalleVentaMovil> getAsientosOcupadosInicio(Long idAutobus, String fecha, int origen, int destino,
            String hora);

    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n"
            // + "AND TIME(fecha_viaje)=?5\r\n"
            + "AND b.t_terminales_origen<>?3 AND b.t_terminales_destino=?4\r\n"
            + "AND a.estatus_disponible=1", nativeQuery = true)
    List<DetalleVentaMovil> getAsientosOcupadosInicioDestino(Long idAutobus, String fecha, int origen, int destino,
            String hora);

    @Query(value = "SELECT DISTINCT a.* " +
            "FROM detalle_venta AS a " +
            "JOIN t_venta_viajes AS b ON a.t_venta_viajes = b.idt_venta_viajes " +
            "JOIN t_ruta AS c ON b.t_ruta = c.idt_ruta " +
            "JOIN t_detalle_ruta AS dr ON c.idt_ruta = dr.t_ruta " +
            "JOIN t_afiliado AS af ON c.t_afiliado = af.idt_afiliado " +
            "WHERE a.t_autobus = :idAutobus " +
            "AND dr.secuencia > :secuencia " +
            "AND (DATE(a.fecha_viaje) = :fecha " +
            "OR (b.t_terminales_origen = :origen OR b.t_terminales_destino = :destino) " +
            "AND a.estatus_disponible = 1) " +
            "AND EXISTS (" +
            "    SELECT 1 " +
            "    FROM detalle_venta AS dv " +
            "    JOIN t_venta_viajes AS tv ON dv.t_venta_viajes = tv.idt_venta_viajes " +
            "    WHERE (tv.t_terminales_origen = :origen AND tv.t_terminales_destino = :destino) " +
            "    AND dv.t_autobus = a.t_autobus " +
            "    AND DATE(dv.fecha_viaje) = :fecha " +
            "    AND dv.estatus_disponible = 1" +
            ")", nativeQuery = true)
    List<DetalleVentaMovil> getAsientosOcupadosGeneral(Long idAutobus, String fecha, int origen, int destino,
            int secuencia);

    @Query(value = "SELECT a.*\r\n"
            + "FROM detalle_venta AS a\r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes=b.idt_venta_viajes\r\n"
            + "JOIN t_ruta AS c ON b.t_ruta=c.idt_ruta\r\n"
            + "WHERE a.t_autobus=?1 AND DATE(fecha_viaje)=?2\r\n"
            + "AND b.t_terminales_origen<>?3 AND b.t_terminales_destino=?4\r\n"
            + "AND a.estatus_disponible=1"
    /* + "AND TIME(fecha_viaje)=?5" */, nativeQuery = true)
    List<DetalleVentaMovil> getAsientosOcupadosFinal(Long idAutobus, String fecha, int origen, int destino);

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
    /* + "AND TIME(fecha_viaje)=?5" */, nativeQuery = true)
    List<DetalleVentaMovil> getAsientosOcupadosMedio(Long idAutobus, String fecha, int origen, int destino);

    List<DetalleVentaMovil> findByVentaViajesIdVentaViaje(Long idVentaViaje);

    @Query(value = "SELECT dv.* " +
            "FROM detalle_venta dv " +
            "JOIN t_venta_viajes vv ON dv.t_venta_viajes = vv.idt_venta_viajes " +
            "WHERE vv.fecha_compra = ?1 AND dv.t_usuarios_final = ?2", nativeQuery = true)
    List<DetalleVentaMovil> findByFechaCompraVentaViajes(String fechaCompra, Long idUsuario);

}
